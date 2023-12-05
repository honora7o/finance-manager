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
public class findMonthlyExpensesTotalByCategoryQuery {
    private final TransactionRepository transactionRepository;

    public findMonthlyExpensesTotalByCategoryQuery(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<String, BigDecimal> execute(TransactionCategoryEnum category, Optional<Integer> month, Optional<Integer> year) {
        LocalDate periodDate = resolveDate(month, year);
        return groupAndSumTransactions(transactionRepository.findAll(), periodDate.getMonthValue(), periodDate.getYear(), category);
    }

    private LocalDate resolveDate(Optional<Integer> month, Optional<Integer> year) {
        return LocalDate.of(
                year.orElse(LocalDate.now().getYear()),
                month.orElse(LocalDate.now().getMonthValue()),
                1
        );
    }

    private boolean isTransactionInPeriod(Transaction transaction, int month, int year, TransactionCategoryEnum category) {
        return transaction.category() == category &&
                transaction.date().getMonthValue() == month &&
                transaction.date().getYear() == year;
    }

    private Map<String, BigDecimal> groupAndSumTransactions(List<Transaction> transactions, int month, int year, TransactionCategoryEnum category) {
        return transactions.stream()
                .filter(transaction -> isTransactionInPeriod(transaction, month, year, category))
                .collect(Collectors.groupingBy(
                        transaction -> String.format("%s total expenses for the period(YYYY/MM) %d/%02d",
                                transaction.category().toString(), year, month),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::value, BigDecimal::add)
                ));
    }
}
