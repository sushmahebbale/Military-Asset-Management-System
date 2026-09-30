package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Base;
import com.militaryams.backend.repository.BaseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bases")
public class BaseController {

    private final BaseRepository baseRepository;

    public BaseController(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @PostMapping
    public Base createBase(@RequestBody Base base) {
        return baseRepository.save(base);
    }

    @GetMapping
    public List<Base> getAllBases() {
        return baseRepository.findAll();
    }

    @PutMapping("/{id}")
    public Base updateBase(@PathVariable Long id, @RequestBody Base base) {
        Base existingBase = baseRepository.findById(id).orElseThrow();

        existingBase.setBaseName(base.getBaseName());
        existingBase.setLocation(base.getLocation());
        existingBase.setBaseType(base.getBaseType());

        return baseRepository.save(existingBase);
    }

    @DeleteMapping("/{id}")
    public void deleteBase(@PathVariable Long id) {
        baseRepository.deleteById(id);
    }
}