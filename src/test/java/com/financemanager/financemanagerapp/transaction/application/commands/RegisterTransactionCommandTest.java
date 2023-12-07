package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import com.financemanager.financemanagerapp.transaction.domain.TransactionPaymentTypeEnum;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RegisterTransactionCommandTest {

    @Test
    void executeTransactionWithNoInstallments() {
        TransactionRepository mockRepository = Mockito.mock(TransactionRepository.class);
        RegisterTransactionCommand command = new RegisterTransactionCommand(mockRepository);

        Transaction transaction = new Transaction.TransactionBuilder()
                .withValue(BigDecimal.TEN)
                .withDescription("Test Transaction")
                .withCategory(TransactionCategoryEnum.EDUCATION)
                .withPaymentType(TransactionPaymentTypeEnum.BALANCE)
                .withDate(LocalDate.now())
                .withInstallmentsTerms(Optional.empty())
                .build();

        command.execute(transaction);

        verify(mockRepository).save(transaction);
    }

    @Test
    void executeTransactionWithInstallments() {
        TransactionRepository mockRepository = Mockito.mock(TransactionRepository.class);
        RegisterTransactionCommand command = new RegisterTransactionCommand(mockRepository);

        BigDecimal transactionValue = BigDecimal.valueOf(500);
        Integer installmentTermsAmount = 2;
        Transaction transaction = new Transaction.TransactionBuilder()
                .withValue(transactionValue)
                .withDescription("Test Transaction")
                .withCategory(TransactionCategoryEnum.EDUCATION)
                .withPaymentType(TransactionPaymentTypeEnum.BALANCE)
                .withDate(LocalDate.now())
                .withInstallmentsTerms(Optional.of(installmentTermsAmount))
                .build();

        command.execute(transaction);

        List<Transaction> installments = command.splitTransactionIntoInstallments(transaction);
        verify(mockRepository).saveAll(installments);

        assertEquals(installments.size(), installmentTermsAmount);

        IntStream.range(0, installments.size())
                .forEach(i -> {
                    Transaction currentInstallment = installments.get(i);
                    assertEquals(String.format("Test Transaction %d/%d", i + 1, installmentTermsAmount),
                            currentInstallment.description());
                    assertEquals(transaction.value().divide(BigDecimal.valueOf(installmentTermsAmount),
                            2, RoundingMode.UNNECESSARY), currentInstallment.value());
                });
    }
}
