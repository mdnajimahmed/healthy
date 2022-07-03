package com.example.healthy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogGeneratorService {
    public void generateLog(int hint) throws Exception {
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
    }
}
