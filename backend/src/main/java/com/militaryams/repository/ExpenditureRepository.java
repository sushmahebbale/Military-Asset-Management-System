package com.militaryams.backend.repository;

import com.militaryams.backend.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    List<Expenditure> findByBase(String base);
}