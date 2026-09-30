package com.militaryams.backend.repository;

import com.militaryams.backend.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findByFromBaseOrToBase(String fromBase, String toBase);
}