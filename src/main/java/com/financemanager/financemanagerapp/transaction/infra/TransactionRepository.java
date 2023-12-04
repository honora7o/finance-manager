package com.financemanager.financemanagerapp.transaction.infra;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
}
