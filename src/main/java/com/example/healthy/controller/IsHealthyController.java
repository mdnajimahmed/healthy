package com.example.healthy.controller;

import com.example.healthy.repository.InMemoryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IsHealthyController {
    private final InMemoryCache inMemoryCache;

    public IsHealthyController(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @GetMapping({"/health", "/"})
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/health/flip")
    public ResponseEntity flipHealth() {
        inMemoryCache.setHealthy(!inMemoryCache.isHealthy());
        return ResponseEntity.ok().build();
    }
}
