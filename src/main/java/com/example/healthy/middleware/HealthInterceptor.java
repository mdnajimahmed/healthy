package com.example.healthy.middleware;

import com.example.healthy.repository.InMemoryCache;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HealthInterceptor implements HandlerInterceptor {
    private final InMemoryCache inMemoryCache;

    public HealthInterceptor(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (inMemoryCache.isHealthy() || request.getRequestURI().contains("/health/flip")) {
            return true;
        }
        response.setStatus(503);
        return false;
    }
}
