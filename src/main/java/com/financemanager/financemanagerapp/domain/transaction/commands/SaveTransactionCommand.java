package com.financemanager.financemanagerapp.domain.transaction.commands;

import com.financemanager.financemanagerapp.domain.transaction.Transaction;
import com.financemanager.financemanagerapp.domain.transaction.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class SaveTransactionCommand {
    private final TransactionRepository transactionRepository;

    public SaveTransactionCommand(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void execute(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}