package com.loadbalancer.proxy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loadbalancer.proxy.service.RoundRobinService;

@RestController
public class LoadBalancerController {

    private final RoundRobinService roundRobinService;

    public LoadBalancerController(RoundRobinService roundRobinService) {
        this.roundRobinService = roundRobinService;
    }

    @GetMapping("/api/request")
    public ResponseEntity<String> handleRequest() {
        return roundRobinService.forwardRequest();
    }

    @GetMapping("/api/dashboard/status")
    public Map<String, Object> dashboardStatus() {
        return roundRobinService.getDashboardStatus();
    }
}
