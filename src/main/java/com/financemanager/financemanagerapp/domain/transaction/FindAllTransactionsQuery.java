package com.financemanager.financemanagerapp.domain.transaction;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllTransactionsQuery {
    private final TransactionRepository transactionRepository;

    public FindAllTransactionsQuery(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> execute() {
        return this.transactionRepository.findAll();
    }
}
