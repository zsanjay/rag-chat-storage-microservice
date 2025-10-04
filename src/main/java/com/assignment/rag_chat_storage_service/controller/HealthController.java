package com.assignment.rag_chat_storage_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Application Health Check")
public class HealthController {

    @GetMapping
    @Operation(summary = "Get Application Health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }
}