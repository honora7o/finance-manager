package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class RegisterTransactionCommand {
    private final TransactionRepository transactionRepository;

    public RegisterTransactionCommand(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void execute(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}