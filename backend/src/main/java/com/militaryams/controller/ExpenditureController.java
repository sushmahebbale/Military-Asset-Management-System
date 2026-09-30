package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Expenditure;
import com.militaryams.backend.entity.User;
import com.militaryams.backend.repository.ExpenditureRepository;
import com.militaryams.backend.repository.UserRepository;
import com.militaryams.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public ExpenditureController(
            ExpenditureRepository expenditureRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.expenditureRepository = expenditureRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public Expenditure createExpenditure(
            @RequestBody Expenditure expenditure,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())) {
            expenditure.setBase(user.getAssignedBase());
        }

        Expenditure savedExpenditure =
                expenditureRepository.save(expenditure);

        auditLogService.log(
                "EXPENDITURE_CREATED",
                authentication.getName(),
                "Expenditure ID: " + savedExpenditure.getId()
                        + ", Base: " + savedExpenditure.getBase()
                        + ", Equipment: " + savedExpenditure.getEquipmentType()
                        + ", Quantity: " + savedExpenditure.getQuantity()
        );

        return savedExpenditure;
    }

    @GetMapping
    public List<Expenditure> getAllExpenditures(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())) {
            return expenditureRepository.findByBase(
                    user.getAssignedBase());
        }

        return expenditureRepository.findAll();
    }

    @PutMapping("/{id}")
    public Expenditure updateExpenditure(
            @PathVariable Long id,
            @RequestBody Expenditure expenditure,
            Authentication authentication) {

        Expenditure existingExpenditure =
                expenditureRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingExpenditure.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        existingExpenditure.setDate(expenditure.getDate());
        existingExpenditure.setEquipmentType(
                expenditure.getEquipmentType());
        existingExpenditure.setReason(
                expenditure.getReason());
        existingExpenditure.setQuantity(
                expenditure.getQuantity());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            existingExpenditure.setBase(user.getAssignedBase());
        } else {
            existingExpenditure.setBase(expenditure.getBase());
        }

        Expenditure savedExpenditure =
                expenditureRepository.save(existingExpenditure);

        auditLogService.log(
                "EXPENDITURE_UPDATED",
                authentication.getName(),
                "Expenditure ID: " + savedExpenditure.getId()
                        + ", Base: " + savedExpenditure.getBase()
                        + ", Equipment: " + savedExpenditure.getEquipmentType()
                        + ", Quantity: " + savedExpenditure.getQuantity()
        );

        return savedExpenditure;
    }

    @DeleteMapping("/{id}")
    public void deleteExpenditure(
            @PathVariable Long id,
            Authentication authentication) {

        Expenditure existingExpenditure =
                expenditureRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingExpenditure.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        expenditureRepository.deleteById(id);

        auditLogService.log(
                "EXPENDITURE_DELETED",
                authentication.getName(),
                "Expenditure ID: " + id
                        + ", Base: " + existingExpenditure.getBase()
                        + ", Equipment: " + existingExpenditure.getEquipmentType()
                        + ", Quantity: " + existingExpenditure.getQuantity()
        );
    }
}