package com.financemanager.financemanagerapp.transaction.infra;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    public List<Transaction> findByCategoryAndPeriod(TransactionCategoryEnum category, int month, int year) {
        return this.transactions.stream()
                .filter(transaction -> isTransactionInPeriod(transaction, month, year, category))
                .collect(Collectors.toList());
    }

    private boolean isTransactionInPeriod(Transaction transaction, int month, int year, TransactionCategoryEnum category) {
        return transaction.category() == category &&
                transaction.date().getMonthValue() == month &&
                transaction.date().getYear() == year;
    }
}
