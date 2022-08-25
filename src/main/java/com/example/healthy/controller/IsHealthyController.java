package com.example.healthy.controller;

import com.example.healthy.repository.InMemoryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class IsHealthyController {
    private final InMemoryCache inMemoryCache;
    @Value("${app.config.pingLog}") private boolean pingLog;

    public IsHealthyController(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @GetMapping({"/health", "/","/ready","/live"})
    public ResponseEntity getHealth(HttpServletRequest request) {
        String path = request.getRequestURI();
        if(pingLog){
            log.info("ping received at {}",path );
        }
        return ResponseEntity.ok("ok " + path);
    }

    @GetMapping("/health/flip")
    public ResponseEntity flipHealth() {
        inMemoryCache.setHealthy(!inMemoryCache.isHealthy());
        return ResponseEntity.ok().build();
    }
}
