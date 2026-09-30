package com.militaryams.backend.controller;

import com.militaryams.backend.entity.AuditLog;
import com.militaryams.backend.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @PostMapping
    public AuditLog createAuditLog(@RequestBody AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @PutMapping("/{id}")
    public AuditLog updateAuditLog(@PathVariable Long id, @RequestBody AuditLog auditLog) {
        AuditLog existingAuditLog = auditLogRepository.findById(id).orElseThrow();

        existingAuditLog.setAction(auditLog.getAction());
        existingAuditLog.setUsername(auditLog.getUsername());
        existingAuditLog.setDate(auditLog.getDate());
        existingAuditLog.setDetails(auditLog.getDetails());

        return auditLogRepository.save(existingAuditLog);
    }

    @DeleteMapping("/{id}")
    public void deleteAuditLog(@PathVariable Long id) {
        auditLogRepository.deleteById(id);
    }
}