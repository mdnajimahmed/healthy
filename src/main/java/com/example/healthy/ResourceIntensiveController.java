package com.example.healthy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceIntensiveController {
    private final DummyRepository dummyRepository;
    private final InMemoryCache inMemoryCache;

    public ResourceIntensiveController(DummyRepository dummyRepository, InMemoryCache inMemoryCache) {
        this.dummyRepository = dummyRepository;
        this.inMemoryCache = inMemoryCache;
    }

    @GetMapping("/sum")
    public ResponseEntity<Double> getSum(@RequestParam("s") int start, @RequestParam("e") int end) {
        if (!inMemoryCache.isHealthy()) {
            return ResponseEntity.internalServerError().build();
        }
        double result = 0;
        for (int i = start; i <= end; i++) {
            result += Math.sqrt(i);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/query")
    public ResponseEntity<String> getPgInfo() {
        if (!inMemoryCache.isHealthy()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(dummyRepository.runQuery());
    }
}
