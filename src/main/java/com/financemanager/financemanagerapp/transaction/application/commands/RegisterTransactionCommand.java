package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RegisterTransactionCommand {
    private final TransactionRepository transactionRepository;

    public RegisterTransactionCommand(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void execute(Transaction transaction) {
        if (transaction.installmentsTerms().isPresent()) {
            List<Transaction> installments = this.splitTransactionIntoInstallments(transaction);
            this.transactionRepository.saveAll(installments);
        } else {
            this.transactionRepository.save(transaction);
        }
    }

    private List<Transaction> splitTransactionIntoInstallments(Transaction transaction) {
        List<Transaction> installments = new ArrayList<>();
        Integer installmentAmount = transaction.installmentsTerms().get();
        BigDecimal installmentValue = transaction.value().divide(BigDecimal.valueOf(installmentAmount), 2, RoundingMode.UNNECESSARY);

        for (int i = 0; i < installmentAmount; i++) {
            installments.add(new Transaction(
                    installmentValue,
                    transaction.description() + " " + (i+1) + "/" + installmentAmount,
                    transaction.category(),
                    transaction.paymentType(),
                    transaction.date().plusMonths(i),
                    Optional.of(installmentAmount))
            );
        }

        return installments;
    }
}