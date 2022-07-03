package com.example.healthy.controller;

import com.example.healthy.service.LogGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogGeneratorController {
    private final LogGeneratorService logGeneratorService;

    public LogGeneratorController(LogGeneratorService logGeneratorService) {
        this.logGeneratorService = logGeneratorService;
    }

    @GetMapping("/generate-log")
    public ResponseEntity generateLog(@RequestParam("h") int hint) throws Exception {
        logGeneratorService.generateLog(hint);
        return ResponseEntity.ok().build();
    }
}

