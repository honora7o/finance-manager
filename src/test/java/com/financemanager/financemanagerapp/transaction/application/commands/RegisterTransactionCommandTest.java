package com.financemanager.financemanagerapp.transaction.application.commands;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import com.financemanager.financemanagerapp.transaction.domain.TransactionPaymentTypeEnum;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class RegisterTransactionCommandTest {

    private TransactionRepository mockRepository;
    private RegisterTransactionCommand command;

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(TransactionRepository.class);
        command = new RegisterTransactionCommand(mockRepository);
    }

    @Test
    void executeTransactionWithNoInstallments() {
        BigDecimal transactionValue = BigDecimal.valueOf(500);
        Transaction transaction = buildTransactionFrom(transactionValue, "Test Transaction", LocalDate.now(), null);

        command.execute(transaction);
        verify(mockRepository).save(transaction);
    }

    @Test
    void executeTransactionWithInstallments() {
        BigDecimal transactionValue = BigDecimal.valueOf(500);
        Integer installmentTermsAmount = 2;
        BigDecimal installmentValue = transactionValue.divide(BigDecimal.valueOf(installmentTermsAmount), 2, RoundingMode.UNNECESSARY);
        LocalDate currentDate = LocalDate.now();

        Transaction transaction = buildTransactionFrom(transactionValue, "Test Transaction", currentDate, installmentTermsAmount);

        Transaction firstTransactionInstallment = buildTransactionFrom(installmentValue, "Test Transaction 1/2", currentDate.plusMonths(1), null);
        Transaction secondTransactionInstallment = buildTransactionFrom(installmentValue, "Test Transaction 2/2", currentDate.plusMonths(2), null);

        command.execute(transaction);
        verify(mockRepository).saveAll(List.of(firstTransactionInstallment, secondTransactionInstallment));
    }

    private Transaction buildTransactionFrom(BigDecimal value, String description, LocalDate date, Integer installmentTerms ) {
        Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder()
            .withValue(value)
            .withDescription(description)
            .withCategory(TransactionCategoryEnum.EDUCATION)
            .withPaymentType(TransactionPaymentTypeEnum.BALANCE)
            .withDate(date);

       return installmentTerms != null
           ? transactionBuilder.withInstallmentsTerms(installmentTerms).build()
           : transactionBuilder.build();
    }
}
