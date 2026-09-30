package com.militaryams.backend.repository;

import com.militaryams.backend.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, Long> {
}