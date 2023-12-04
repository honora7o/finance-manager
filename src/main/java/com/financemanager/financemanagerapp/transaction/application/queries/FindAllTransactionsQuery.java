package com.financemanager.financemanagerapp.transaction.application.queries;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
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
