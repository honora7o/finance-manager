package com.financemanager.financemanagerapp.transaction.application.queries;

import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Component
public class findMonthlyExpensesTotalByCategoryQuery {
    private final TransactionRepository transactionRepository;

    public findMonthlyExpensesTotalByCategoryQuery(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<String, BigDecimal> execute(TransactionCategoryEnum category, Optional<Integer> month, Optional<Integer> year) {
        Integer resolvedMonth = month.orElse(LocalDate.now().getMonthValue());
        Integer resolvedYear = year.orElse(LocalDate.now().getYear());
        return this.transactionRepository.findMonthlyExpensesTotalByCategory(category, resolvedMonth, resolvedYear);
    }

}
