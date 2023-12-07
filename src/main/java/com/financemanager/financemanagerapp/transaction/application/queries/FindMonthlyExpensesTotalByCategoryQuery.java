package com.financemanager.financemanagerapp.transaction.application.queries;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FindMonthlyExpensesTotalByCategoryQuery {
    private final TransactionRepository transactionRepository;

    public FindMonthlyExpensesTotalByCategoryQuery(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<String, BigDecimal> execute(TransactionCategoryEnum category, Optional<Integer> month, Optional<Integer> year) {
        LocalDate periodDate = resolveDate(month, year);
        List<Transaction> filteredTransactions = transactionRepository.findByCategoryAndPeriod(category, periodDate.getMonthValue(), periodDate.getYear());

        var result = groupAndSumTransactions(filteredTransactions, category, periodDate);

        return result.isEmpty() ?
                Map.of(buildMapKey(category, periodDate), BigDecimal.ZERO) :
                result;
    }

    private LocalDate resolveDate(Optional<Integer> month, Optional<Integer> year) {
        return LocalDate.of(
                year.orElse(LocalDate.now().getYear()),
                month.orElse(LocalDate.now().getMonthValue()),
                1
        );
    }

    private Map<String, BigDecimal> groupAndSumTransactions(List<Transaction> transactions, TransactionCategoryEnum category, LocalDate periodDate) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> buildMapKey(transaction.category(), transaction.date()),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::value, BigDecimal::add)
                ));
    }

    private String buildMapKey(TransactionCategoryEnum category, LocalDate date) {
        return String.format("%s total expenses for the period(YYYY/MM) %d/%02d",
                category.toString(), date.getYear(), date.getMonthValue());
    }
}
