package com.financemanager.financemanagerapp.transaction.infra;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public void saveAll(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
    }

    public List<Transaction> findAll() {
        return this.transactions;
    }

    public Map<String, BigDecimal> findMonthlyExpensesTotalByCategory(TransactionCategoryEnum category, Integer month, Integer year) {
        Map<String, BigDecimal> result = transactions.stream()
                .filter(transaction ->
                        transaction.category() == category &&
                                transaction.date().getMonthValue() == month &&
                                transaction.date().getYear() == year)
                .collect(Collectors.groupingBy(transaction ->
                                String.format("%s total expenses for the period(YYYY/MM) %d/%02d",
                                        transaction.category().toString(), year, month),
                                Collectors.reducing(BigDecimal.ZERO, Transaction::value, BigDecimal::add)
                ));

        return result.isEmpty() ?
                Map.of(String.format("%s total expenses for the period(YYYY/MM) %d/%02d",
                        category.toString(), year, month), BigDecimal.ZERO) :
                result;
    }
}
