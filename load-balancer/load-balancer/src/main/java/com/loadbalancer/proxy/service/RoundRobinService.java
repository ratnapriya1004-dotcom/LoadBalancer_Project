package com.loadbalancer.proxy.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoundRobinService {

    private final List<String> servers = List.of(
        "http://localhost:8081",
        "http://localhost:8082"
    );

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final Map<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();

    public RoundRobinService() {
        for (String server : servers) {
            requestCounts.put(server, new AtomicLong(0));
        }
    }

    public ResponseEntity<String> forwardRequest() {
        int startIndex = Math.floorMod(counter.getAndIncrement(), servers.size());

        // Start with the next Round Robin server, then try the other server
        // if the selected backend is unavailable.
        for (int i = 0; i < servers.size(); i++) {
            int index = (startIndex + i) % servers.size();
            String selectedServer = servers.get(index);

            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(selectedServer + "/api/server"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

                HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    requestCounts.get(selectedServer).incrementAndGet();
                    totalRequests.incrementAndGet();
                }

                return ResponseEntity.status(response.statusCode()).body(response.body());

            } catch (IOException e) {
                System.out.println("Backend unavailable: " + selectedServer);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Request interrupted");
            }
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("All backend servers are unavailable!");
    }

    public Map<String, Object> getDashboardStatus() {
        List<Map<String, Object>> serverStatuses = new ArrayList<>();
        int onlineCount = 0;

        for (int i = 0; i < servers.size(); i++) {
            String server = servers.get(i);
            boolean online = isServerOnline(server);
            if (online) {
                onlineCount++;
            }

            Map<String, Object> details = new LinkedHashMap<>();
            details.put("name", "Server " + (i + 1));
            details.put("url", server);
            details.put("online", online);
            details.put("requestsHandled", requestCounts.get(server).get());
            serverStatuses.add(details);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("algorithm", "Round Robin");
        result.put("onlineServers", onlineCount);
        result.put("totalServers", servers.size());
        result.put("totalRequests", totalRequests.get());
        result.put("servers", serverStatuses);
        return result;
    }

    private boolean isServerOnline(String server) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server + "/api/server"))
                .timeout(Duration.ofSeconds(1))
                .GET()
                .build();

            HttpResponse<Void> response = httpClient.send(
                request, HttpResponse.BodyHandlers.discarding()
            );
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
