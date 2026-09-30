package com.militaryams.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String fromBase;
    private String toBase;
    private String equipmentType;
    private int quantity;
    private LocalDateTime transferTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromBase() {
        return fromBase;
    }

    public void setFromBase(String fromBase) {
        this.fromBase = fromBase;
    }

    public String getToBase() {
        return toBase;
    }

    public void setToBase(String toBase) {
        this.toBase = toBase;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTransferTimestamp() {
        return transferTimestamp;
    }

    public void setTransferTimestamp(LocalDateTime transferTimestamp) {
        this.transferTimestamp = transferTimestamp;
    }
}