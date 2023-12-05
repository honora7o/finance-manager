package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Integer installmentTermsAmount = getInstallmentTermsAmount(transaction);
        BigDecimal installmentValue = getInstallmentValue(transaction);

        return IntStream.range(0, installmentTermsAmount)
                .mapToObj(i -> new Transaction.TransactionBuilder()
                        .withValue(installmentValue)
                        // vale a pena abstrair a logica pra criar descriçao? parece giga overengineering
                        .withDescription(transaction.description() + " " + (i + 1) + "/" + installmentTermsAmount)
                        .withCategory(transaction.category())
                        .withPaymentType(transaction.paymentType())
                        .withDate(transaction.date())
                        .build())
                .collect(Collectors.toList());
    }

    private Integer getInstallmentTermsAmount(Transaction transaction) {
        return transaction.installmentsTerms().get();
    }

    private BigDecimal getInstallmentValue(Transaction transaction) {
        Integer installmentTermsAmount = getInstallmentTermsAmount(transaction);
        return transaction.value().divide(BigDecimal.valueOf(installmentTermsAmount), 2, RoundingMode.UNNECESSARY);
    }
}