package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RegisterTransactionCommand {
    private final TransactionRepository transactionRepository;

    public RegisterTransactionCommand(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void execute(Transaction transaction) {
        if (transaction.installmentsTerms().isEmpty()) {
            this.transactionRepository.save(transaction);
            return;
        }

        List<Transaction> installments = this.splitTransactionIntoInstallments(transaction);
        this.transactionRepository.saveAll(installments);
    }

    private List<Transaction> splitTransactionIntoInstallments(Transaction transaction) {
        Integer installmentTermsAmount = getInstallmentTermsAmount(transaction);
        BigDecimal installmentValue = getInstallmentValue(transaction);

        return IntStream.range(0, installmentTermsAmount)
                .mapToObj(i -> buildInstallment(transaction, i, installmentValue))
                .collect(Collectors.toList());
    }

    private Transaction buildInstallment(Transaction transaction, int index, BigDecimal installmentValue) {
        LocalDate installmentDate = transaction.date().plusMonths(index + 1);

        return new Transaction.TransactionBuilder()
                .withValue(installmentValue)
                .withDescription(buildInstallmentDescription(transaction, index))
                .withCategory(transaction.category())
                .withPaymentType(transaction.paymentType())
                .withDate(installmentDate)
                .build();
    }

    private String buildInstallmentDescription(Transaction transaction, int index) {
        return String.format("%s %d/%d", transaction.description(), (index + 1), getInstallmentTermsAmount(transaction));
    }

    private Integer getInstallmentTermsAmount(Transaction transaction) {
        return transaction.installmentsTerms().orElse(0);
    }

    private BigDecimal getInstallmentValue(Transaction transaction) {
        Integer installmentTermsAmount = getInstallmentTermsAmount(transaction);
        return transaction.value().divide(BigDecimal.valueOf(installmentTermsAmount), 2, RoundingMode.UNNECESSARY);
    }
}