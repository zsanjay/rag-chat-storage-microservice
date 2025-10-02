package com.assignment.rag_chat_storage_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${API_KEYS}")
    private String apiKeys;

    private List<String> validKeys;

    private static final String HEADER_NAME = "x-api-key";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (validKeys == null) {
            validKeys = Arrays.asList(apiKeys.split(","));
        }

        String requestApiKey = request.getHeader(HEADER_NAME);

        if (requestApiKey == null || !validKeys.contains(requestApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid API Key");
            return;
        }

        filterChain.doFilter(request, response);
    }
}