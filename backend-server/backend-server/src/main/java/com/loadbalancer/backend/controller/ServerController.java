
package com.loadbalancer.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/api/server")
    public String serverResponse() {
        return "Hello! Response served by Backend Server on port " + port;
    }
}
