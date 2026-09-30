package com.militaryams.service;

import com.militaryams.backend.entity.AuditLog;
import com.militaryams.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String username, String details) {
        AuditLog auditLog = new AuditLog();

        auditLog.setAction(action);
        auditLog.setUsername(username);
        auditLog.setDate(LocalDate.now().toString());
        auditLog.setDetails(details);

        auditLogRepository.save(auditLog);
    }
}