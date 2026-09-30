package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Assignment;
import com.militaryams.backend.entity.User;
import com.militaryams.backend.repository.AssignmentRepository;
import com.militaryams.backend.repository.UserRepository;
import com.militaryams.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public AssignmentController(
            AssignmentRepository assignmentRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public Assignment createAssignment(
            @RequestBody Assignment assignment,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())) {
            assignment.setBase(user.getAssignedBase());
        }

        Assignment savedAssignment =
                assignmentRepository.save(assignment);

        auditLogService.log(
                "ASSIGNMENT_CREATED",
                authentication.getName(),
                "Assignment ID: " + savedAssignment.getId()
                        + ", Base: " + savedAssignment.getBase()
                        + ", Equipment: " + savedAssignment.getEquipmentType()
                        + ", Assigned To: " + savedAssignment.getAssignedTo()
                        + ", Quantity: " + savedAssignment.getQuantity()
        );

        return savedAssignment;
    }

    @GetMapping
    public List<Assignment> getAllAssignments(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())) {
            return assignmentRepository.findByBase(
                    user.getAssignedBase());
        }

        return assignmentRepository.findAll();
    }

    @PutMapping("/{id}")
    public Assignment updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment,
            Authentication authentication) {

        Assignment existingAssignment =
                assignmentRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingAssignment.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        existingAssignment.setDate(assignment.getDate());
        existingAssignment.setEquipmentType(
                assignment.getEquipmentType());
        existingAssignment.setAssignedTo(
                assignment.getAssignedTo());
        existingAssignment.setQuantity(
                assignment.getQuantity());

        if ("BASE_COMMANDER".equals(user.getRole())) {
            existingAssignment.setBase(user.getAssignedBase());
        } else {
            existingAssignment.setBase(assignment.getBase());
        }

        Assignment savedAssignment =
                assignmentRepository.save(existingAssignment);

        auditLogService.log(
                "ASSIGNMENT_UPDATED",
                authentication.getName(),
                "Assignment ID: " + savedAssignment.getId()
                        + ", Base: " + savedAssignment.getBase()
                        + ", Equipment: " + savedAssignment.getEquipmentType()
                        + ", Assigned To: " + savedAssignment.getAssignedTo()
                        + ", Quantity: " + savedAssignment.getQuantity()
        );

        return savedAssignment;
    }

    @DeleteMapping("/{id}")
    public void deleteAssignment(
            @PathVariable Long id,
            Authentication authentication) {

        Assignment existingAssignment =
                assignmentRepository.findById(id).orElseThrow();

        User user = userRepository.findByUsername(authentication.getName());

        if ("LOGISTICS_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        if ("BASE_COMMANDER".equals(user.getRole())
                && !user.getAssignedBase()
                .equals(existingAssignment.getBase())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        assignmentRepository.deleteById(id);

        auditLogService.log(
                "ASSIGNMENT_DELETED",
                authentication.getName(),
                "Assignment ID: " + id
                        + ", Base: " + existingAssignment.getBase()
                        + ", Equipment: " + existingAssignment.getEquipmentType()
                        + ", Assigned To: " + existingAssignment.getAssignedTo()
                        + ", Quantity: " + existingAssignment.getQuantity()
        );
    }
}