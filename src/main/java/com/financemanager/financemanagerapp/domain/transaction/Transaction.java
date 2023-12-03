package com.financemanager.financemanagerapp.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(
        BigDecimal value,
        String description,
        String category,
        String paymentType,
        LocalDate date
) {}
