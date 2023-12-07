package com.financemanager.financemanagerapp.transaction.domain;

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
) {
    public static class TransactionBuilder {
        private BigDecimal value;
        private String description;
        private TransactionCategoryEnum category;
        private TransactionPaymentTypeEnum paymentType;
        private LocalDate date;
        private Optional<Integer> installmentsTerms;

        public TransactionBuilder withValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public TransactionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TransactionBuilder withCategory(TransactionCategoryEnum category) {
            this.category = category;
            return this;
        }

        public TransactionBuilder withPaymentType(TransactionPaymentTypeEnum paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public TransactionBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public TransactionBuilder withInstallmentsTerms(Optional<Integer> installmentsTerms) {
            this.installmentsTerms = installmentsTerms;
            return this;
        }

        public Transaction build() {
            return new Transaction(value, description, category, paymentType, date, installmentsTerms);
        }
    }
}

