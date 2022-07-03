package com.example.healthy;

import com.example.healthy.service.SlowDbQueryService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
class HealthyApplicationTests {
    @Autowired
    private SlowDbQueryService slowDbQueryService;

    @Test
    void contextLoads() {

    }

    @Test
    void checkPostgresSetup() {
        Assertions.assertTrue(slowDbQueryService.runQuery().toLowerCase().contains("postgresql"));

    }

}
