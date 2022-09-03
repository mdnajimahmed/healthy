package com.example.healthy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnvironmentVariableDebug implements CommandLineRunner {
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUserName;
    @Value("${spring.datasource.password}")
    private String dbPassword;


    @Override
    public void run(String... args) throws Exception {
        log.info("serverPort = {}",serverPort);
        log.info("dbUrl = {}",dbUrl);
        log.info("dbUserName = {}",dbUserName);
        log.info("dbPassword = {}",dbPassword);
        log.info("DB_URL = {}",System.getenv("DB_URL"));
        log.info("DB_USERNAME = {}",System.getenv("DB_USERNAME"));
        log.info("DB_PASSWORD = {}",System.getenv("DB_PASSWORD"));
        log.info("DB_POOL_SIZE = {}",System.getenv("DB_POOL_SIZE"));
        log.info("PING_LOG = {}",System.getenv("PING_LOG"));
        log.info("MY_POD_NAME = {}",System.getenv("MY_POD_NAME"));
        log.info("MY_POD_NAMESPACE = {}",System.getenv("MY_POD_NAMESPACE"));
        log.info("MY_POD_IP = {}",System.getenv("MY_POD_IP"));
    }
}
