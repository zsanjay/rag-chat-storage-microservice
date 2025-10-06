package com.assignment.rag_chat_storage_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Application Health Check")
@Slf4j
public class HealthController {

    @GetMapping
    @Operation(summary = "Get Application Health")
    public ResponseEntity<String> health() {
        log.info("Health check endpoint called");

        String status = "UP";
        log.debug("Application status: {}", status);

        return ResponseEntity.ok(status);
    }
}