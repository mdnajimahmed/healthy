package com.example.healthy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class LogGeneratorController {
    private Random random = new Random();

    @GetMapping("/generate-log")
    public ResponseEntity getSum(@RequestParam("h") int hint) throws Exception {
        // generate
        if (hint == 0) {
            log.trace("fake trace message");
        } else if (hint == 1) {
            log.debug("fake debug message");
        } else if (hint == 2) {
            log.info("fake info message");
        } else if (hint == 3) {
            log.warn("fake warn message");
        } else if (hint == 4) {
            log.error("fake error message", new ArrayIndexOutOfBoundsException());
        } else if (hint == 5) {
            log.error("fake error message", new RuntimeException("Unknown Runtime Exception"));
        }
        else{
            throw new Exception("Unknown hint");
        }
        return ResponseEntity.ok().build();
    }
}

