package com.example.healthy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryStressController {
    List<Integer> nonsenseMemory = new ArrayList<>();

    @GetMapping("/memory-stress")
    public ResponseEntity<Integer> generateNumber() {
        for (int i = 0; i < 1024 * 1024; i++) {
            nonsenseMemory.add(i);
        }
        return ResponseEntity.ok((nonsenseMemory.size() * 4) / (1024 * 1024));
    }
}

