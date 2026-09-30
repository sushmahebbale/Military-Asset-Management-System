package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Equipment;
import com.militaryams.backend.repository.EquipmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    public EquipmentController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @PostMapping
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @PutMapping("/{id}")
    public Equipment updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        Equipment existingEquipment = equipmentRepository.findById(id).orElseThrow();

        existingEquipment.setEquipmentName(equipment.getEquipmentName());
        existingEquipment.setEquipmentType(equipment.getEquipmentType());
        existingEquipment.setSerialNumber(equipment.getSerialNumber());
        existingEquipment.setStatus(equipment.getStatus());

        return equipmentRepository.save(existingEquipment);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentRepository.deleteById(id);
    }
}