package com.financemanager.financemanagerapp.domain.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public record Transaction(
        BigDecimal value,
        String description,
        TransactionCategoryEnum category,
        TransactionPaymentTypeEnum paymentType,
        LocalDate date,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Optional<Integer> installmentsTerms
) {}

