package com.example.healthy.controller;

import com.example.healthy.service.MemoryStressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryStressController {
    private final MemoryStressService memoryStressService;
    List<Integer> nonsenseMemory = new ArrayList<>();

    public MemoryStressController(MemoryStressService memoryStressService) {
        this.memoryStressService = memoryStressService;
    }

    @GetMapping("/memory-stress")
    public ResponseEntity<Integer> generateNumber(@RequestParam(value = "s", required = false, defaultValue = "1") int step) {
        return ResponseEntity.ok(memoryStressService.generateNumber(step));
    }
}

