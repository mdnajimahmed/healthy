package com.example.healthy.controller;

import com.example.healthy.service.SlowDbQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlowDbQueryController {
    private final SlowDbQueryService slowDbQueryService;

    public SlowDbQueryController(SlowDbQueryService slowDbQueryService) {
        this.slowDbQueryService = slowDbQueryService;
    }


    @GetMapping("/slow-query")
    public ResponseEntity<String> getPgInfo() {
        return ResponseEntity.ok(slowDbQueryService.runQuery());
    }
}
