package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Purchase;
import com.militaryams.backend.entity.User;
import com.militaryams.backend.repository.PurchaseRepository;
import com.militaryams.backend.repository.UserRepository;
import com.militaryams.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public PurchaseController(
            PurchaseRepository purchaseRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public Purchase createPurchase(
            @RequestBody Purchase purchase,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            purchase.setBase(user.getAssignedBase());
        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        auditLogService.log(
                "PURCHASE_CREATED",
                authentication.getName(),
                "Purchase ID: " + savedPurchase.getId()
                        + ", Base: " + savedPurchase.getBase()
                        + ", Equipment: " + savedPurchase.getEquipmentType()
                        + ", Quantity: " + savedPurchase.getQuantity()
        );

        return savedPurchase;
    }

    @GetMapping
    public List<Purchase> getAllPurchases(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            return purchaseRepository.findByBase(user.getAssignedBase());
        }

        return purchaseRepository.findAll();
    }

    @PutMapping("/{id}")
    public Purchase updatePurchase(
            @PathVariable Long id,
            @RequestBody Purchase purchase,
            Authentication authentication) {

        Purchase existingPurchase =
                purchaseRepository.findById(id).orElseThrow();

        User user =
                userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingPurchase.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        existingPurchase.setDate(purchase.getDate());
        existingPurchase.setEquipmentType(purchase.getEquipmentType());
        existingPurchase.setQuantity(purchase.getQuantity());
        existingPurchase.setSupplier(purchase.getSupplier());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            existingPurchase.setBase(user.getAssignedBase());
        } else {
            existingPurchase.setBase(purchase.getBase());
        }

        Purchase savedPurchase =
                purchaseRepository.save(existingPurchase);

        auditLogService.log(
                "PURCHASE_UPDATED",
                authentication.getName(),
                "Purchase ID: " + savedPurchase.getId()
                        + ", Base: " + savedPurchase.getBase()
                        + ", Equipment: " + savedPurchase.getEquipmentType()
                        + ", Quantity: " + savedPurchase.getQuantity()
        );

        return savedPurchase;
    }

    @DeleteMapping("/{id}")
    public void deletePurchase(
            @PathVariable Long id,
            Authentication authentication) {

        Purchase existingPurchase =
                purchaseRepository.findById(id).orElseThrow();

        User user =
                userRepository.findByUsername(authentication.getName());

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingPurchase.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        purchaseRepository.deleteById(id);

        auditLogService.log(
                "PURCHASE_DELETED",
                authentication.getName(),
                "Purchase ID: " + id
                        + ", Base: " + existingPurchase.getBase()
                        + ", Equipment: " + existingPurchase.getEquipmentType()
                        + ", Quantity: " + existingPurchase.getQuantity()
        );
    }
}