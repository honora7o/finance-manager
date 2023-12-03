package com.financemanager.financemanagerapp.domain.transaction.repositories;

import com.financemanager.financemanagerapp.domain.transaction.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> findAll() {
        return this.transactions;
    }
}
