package com.assignment.rag_chat_storage_service.config;

import com.assignment.rag_chat_storage_service.constant.Constants;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatAuthFilter extends OncePerRequestFilter {

    private List<String> validApiKeys = new ArrayList<>();
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public ChatAuthFilter() {
        loadApiKeys();
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.simple(Constants.NO_OF_REQUESTS, Duration.ofMinutes(Constants.MINUTES));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

            String requestApiKey = request.getHeader(Constants.API_KEY_HEADER_NAME);

            if (requestApiKey == null || !isValidApiKey(requestApiKey)) {
                log.warn("Invalid API key for request: {} {}", request.getMethod(), request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Unauthorized: Invalid API Key\"}");
                return;
            }

            // Rate Limiting
            Bucket bucket = buckets.computeIfAbsent(requestApiKey, k -> createNewBucket());
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for key ending with '{}'",
                        requestApiKey.substring(Math.max(0, requestApiKey.length() - 4)));
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("{\"error\":\"Rate limit exceeded. Try again later\"}");
                return;
            }

            log.debug("Authorized request for {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/actuator/health");
    }

    private void loadApiKeys() {
        String apiKeys = System.getenv(Constants.API_KEYS);
        log.debug("Loading API Keys from System Property: {}", apiKeys);
        if (apiKeys == null || apiKeys.isEmpty()) {
            throw new IllegalStateException("API_KEYS environment variable is not set!");
        }
        this.validApiKeys = Arrays.asList(apiKeys.split(","));
    }

    private boolean isValidApiKey(String apiKey) {
        return validApiKeys.contains(apiKey);
    }
}
