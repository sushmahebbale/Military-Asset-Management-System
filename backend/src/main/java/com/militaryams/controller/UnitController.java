package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Unit;
import com.militaryams.backend.repository.UnitRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitRepository unitRepository;

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        return unitRepository.save(unit);
    }

    @PutMapping("/{id}")
    public Unit updateUnit(@PathVariable Long id, @RequestBody Unit unit) {
    Unit existingUnit = unitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unit not found"));

    existingUnit.setUnitName(unit.getUnitName());
    existingUnit.setUnitType(unit.getUnitType());
    existingUnit.setLocation(unit.getLocation());
    existingUnit.setCommander(unit.getCommander());

    return unitRepository.save(existingUnit);
}

    @DeleteMapping("/{id}")
    public void deleteUnit(@PathVariable Long id) {
        unitRepository.deleteById(id);
    }
}