package com.financemanager.financemanagerapp.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public record Transaction(
        BigDecimal value,
        String description,
        TransactionCategoryEnum category,
        TransactionPaymentTypeEnum paymentType,
        LocalDate date,
        Optional<Integer> installmentsTerms
) {}

