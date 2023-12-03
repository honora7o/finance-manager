package com.financemanager.financemanagerapp.domain.transaction;

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
