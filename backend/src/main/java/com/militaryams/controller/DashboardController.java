package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Assignment;
import com.militaryams.backend.entity.Expenditure;
import com.militaryams.backend.entity.Purchase;
import com.militaryams.backend.entity.Transfer;
import com.militaryams.backend.repository.AssignmentRepository;
import com.militaryams.backend.repository.ExpenditureRepository;
import com.militaryams.backend.repository.PurchaseRepository;
import com.militaryams.backend.repository.TransferRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final PurchaseRepository purchaseRepository;
    private final TransferRepository transferRepository;
    private final AssignmentRepository assignmentRepository;
    private final ExpenditureRepository expenditureRepository;

    public DashboardController(
            PurchaseRepository purchaseRepository,
            TransferRepository transferRepository,
            AssignmentRepository assignmentRepository,
            ExpenditureRepository expenditureRepository) {

        this.purchaseRepository = purchaseRepository;
        this.transferRepository = transferRepository;
        this.assignmentRepository = assignmentRepository;
        this.expenditureRepository = expenditureRepository;
    }

    @GetMapping
    public Map<String, Object> getDashboard(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String base,
            @RequestParam(required = false) String equipmentType) {

        Map<String, Object> dashboard = new HashMap<>();

        int openingBalance = 0;

        if (date != null && !date.isEmpty()) {

            int openingPurchases = purchaseRepository.findAll()
                    .stream()
                    .filter(p -> p.getDate().compareTo(date) < 0)
                    .filter(p -> base == null || base.isEmpty()
                            || base.equals(p.getBase()))
                    .filter(p -> equipmentType == null || equipmentType.isEmpty()
                            || equipmentType.equals(p.getEquipmentType()))
                    .mapToInt(Purchase::getQuantity)
                    .sum();

            int openingTransferIn = transferRepository.findAll()
                    .stream()
                    .filter(t -> t.getDate().compareTo(date) < 0)
                    .filter(t -> base == null || base.isEmpty()
                            || base.equals(t.getToBase()))
                    .filter(t -> equipmentType == null || equipmentType.isEmpty()
                            || equipmentType.equals(t.getEquipmentType()))
                    .mapToInt(Transfer::getQuantity)
                    .sum();

            int openingTransferOut = transferRepository.findAll()
                    .stream()
                    .filter(t -> t.getDate().compareTo(date) < 0)
                    .filter(t -> base == null || base.isEmpty()
                            || base.equals(t.getFromBase()))
                    .filter(t -> equipmentType == null || equipmentType.isEmpty()
                            || equipmentType.equals(t.getEquipmentType()))
                    .mapToInt(Transfer::getQuantity)
                    .sum();

            int openingExpenditure = expenditureRepository.findAll()
                    .stream()
                    .filter(e -> e.getDate().compareTo(date) < 0)
                    .filter(e -> base == null || base.isEmpty()
                            || base.equals(e.getBase()))
                    .filter(e -> equipmentType == null || equipmentType.isEmpty()
                            || equipmentType.equals(e.getEquipmentType()))
                    .mapToInt(Expenditure::getQuantity)
                    .sum();

            openingBalance = openingPurchases
                    + openingTransferIn
                    - openingTransferOut
                    - openingExpenditure;
        }

        int totalPurchases = purchaseRepository.findAll()
                .stream()
                .filter(p -> date == null || date.isEmpty()
                        || date.equals(p.getDate()))
                .filter(p -> base == null || base.isEmpty()
                        || base.equals(p.getBase()))
                .filter(p -> equipmentType == null || equipmentType.isEmpty()
                        || equipmentType.equals(p.getEquipmentType()))
                .mapToInt(Purchase::getQuantity)
                .sum();

        int totalTransferIn = transferRepository.findAll()
                .stream()
                .filter(t -> date == null || date.isEmpty()
                        || date.equals(t.getDate()))
                .filter(t -> base == null || base.isEmpty()
                        || base.equals(t.getToBase()))
                .filter(t -> equipmentType == null || equipmentType.isEmpty()
                        || equipmentType.equals(t.getEquipmentType()))
                .mapToInt(Transfer::getQuantity)
                .sum();

        int totalTransferOut = transferRepository.findAll()
                .stream()
                .filter(t -> date == null || date.isEmpty()
                        || date.equals(t.getDate()))
                .filter(t -> base == null || base.isEmpty()
                        || base.equals(t.getFromBase()))
                .filter(t -> equipmentType == null || equipmentType.isEmpty()
                        || equipmentType.equals(t.getEquipmentType()))
                .mapToInt(Transfer::getQuantity)
                .sum();

        int totalAssigned = assignmentRepository.findAll()
                .stream()
                .filter(a -> date == null || date.isEmpty()
                        || date.equals(a.getDate()))
                .filter(a -> base == null || base.isEmpty()
                        || base.equals(a.getBase()))
                .filter(a -> equipmentType == null || equipmentType.isEmpty()
                        || equipmentType.equals(a.getEquipmentType()))
                .mapToInt(Assignment::getQuantity)
                .sum();

        int totalExpended = expenditureRepository.findAll()
                .stream()
                .filter(e -> date == null || date.isEmpty()
                        || date.equals(e.getDate()))
                .filter(e -> base == null || base.isEmpty()
                        || base.equals(e.getBase()))
                .filter(e -> equipmentType == null || equipmentType.isEmpty()
                        || equipmentType.equals(e.getEquipmentType()))
                .mapToInt(Expenditure::getQuantity)
                .sum();

        int netMovement = totalPurchases
                + totalTransferIn
                - totalTransferOut;

        int closingBalance;

        if (date == null || date.isEmpty()) {
            closingBalance = netMovement - totalExpended;
        } else {
            closingBalance = openingBalance
                    + netMovement
                    - totalExpended;
        }

        dashboard.put("openingBalance", openingBalance);
        dashboard.put("closingBalance", closingBalance);
        dashboard.put("netMovement", netMovement);
        dashboard.put("assigned", totalAssigned);
        dashboard.put("expended", totalExpended);

        return dashboard;
    }
}