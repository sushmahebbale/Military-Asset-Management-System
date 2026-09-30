package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Transfer;
import com.militaryams.backend.entity.User;
import com.militaryams.backend.repository.TransferRepository;
import com.militaryams.backend.repository.UserRepository;
import com.militaryams.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public TransferController(
            TransferRepository transferRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public Transfer createTransfer(
            @RequestBody Transfer transfer,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            if (!user.getAssignedBase().equals(transfer.getFromBase())
                    && !user.getAssignedBase().equals(transfer.getToBase())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Access denied");
            }
        }

        transfer.setTransferTimestamp(LocalDateTime.now());

        Transfer savedTransfer = transferRepository.save(transfer);

        auditLogService.log(
                "TRANSFER_CREATED",
                authentication.getName(),
                "Transfer ID: " + savedTransfer.getId()
                        + ", From: " + savedTransfer.getFromBase()
                        + ", To: " + savedTransfer.getToBase()
                        + ", Equipment: " + savedTransfer.getEquipmentType()
                        + ", Quantity: " + savedTransfer.getQuantity()
        );

        return savedTransfer;
    }

    @GetMapping
    public List<Transfer> getAllTransfers(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            return transferRepository.findByFromBaseOrToBase(
                    user.getAssignedBase(),
                    user.getAssignedBase()
            );
        }

        return transferRepository.findAll();
    }

    @PutMapping("/{id}")
    public Transfer updateTransfer(
            @PathVariable Long id,
            @RequestBody Transfer transfer,
            Authentication authentication) {

        Transfer existingTransfer =
                transferRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            if (!user.getAssignedBase().equals(existingTransfer.getFromBase())
                    && !user.getAssignedBase().equals(existingTransfer.getToBase())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Access denied");
            }
        }

        existingTransfer.setDate(transfer.getDate());
        existingTransfer.setFromBase(transfer.getFromBase());
        existingTransfer.setToBase(transfer.getToBase());
        existingTransfer.setEquipmentType(transfer.getEquipmentType());
        existingTransfer.setQuantity(transfer.getQuantity());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            if (!user.getAssignedBase().equals(existingTransfer.getFromBase())
                    && !user.getAssignedBase().equals(existingTransfer.getToBase())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Access denied");
            }
        }

        Transfer savedTransfer =
                transferRepository.save(existingTransfer);

        auditLogService.log(
                "TRANSFER_UPDATED",
                authentication.getName(),
                "Transfer ID: " + savedTransfer.getId()
                        + ", From: " + savedTransfer.getFromBase()
                        + ", To: " + savedTransfer.getToBase()
                        + ", Equipment: " + savedTransfer.getEquipmentType()
                        + ", Quantity: " + savedTransfer.getQuantity()
        );

        return savedTransfer;
    }

    @DeleteMapping("/{id}")
    public void deleteTransfer(
            @PathVariable Long id,
            Authentication authentication) {

        Transfer existingTransfer =
                transferRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            if (!user.getAssignedBase().equals(existingTransfer.getFromBase())
                    && !user.getAssignedBase().equals(existingTransfer.getToBase())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Access denied");
            }
        }

        transferRepository.deleteById(id);

        auditLogService.log(
                "TRANSFER_DELETED",
                authentication.getName(),
                "Transfer ID: " + id
                        + ", From: " + existingTransfer.getFromBase()
                        + ", To: " + existingTransfer.getToBase()
                        + ", Equipment: " + existingTransfer.getEquipmentType()
                        + ", Quantity: " + existingTransfer.getQuantity()
        );
    }
}