package com.financemanager.financemanagerapp.transaction.application.queries;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import com.financemanager.financemanagerapp.transaction.domain.TransactionPaymentTypeEnum;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FindMonthlyExpensesTotalByCategoryQueryTest {

    private TransactionRepository mockRepository;
    private FindMonthlyExpensesTotalByCategoryQuery query;

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(TransactionRepository.class);
        query = new FindMonthlyExpensesTotalByCategoryQuery(mockRepository);
    }

    @Test
    void executeShouldReturnExpensesTotalForGivenCategoryAndPeriod() {
        TransactionCategoryEnum category = TransactionCategoryEnum.EDUCATION;
        Optional<Integer> month = Optional.of(5);
        Optional<Integer> year = Optional.of(2022);

        LocalDate periodDate = LocalDate.of(2022, 5, 1);
        List<Transaction> expectedTransactions = Arrays.asList(
                buildTransactionFrom(BigDecimal.TEN, "Test Transaction 1", periodDate, null),
                buildTransactionFrom(BigDecimal.valueOf(20), "Test Transaction 2", periodDate, null)
        );

        when(mockRepository.findByCategoryAndPeriod(category, month.get(), year.get())).thenReturn(expectedTransactions);

        Map<String, BigDecimal> result = query.execute(category, month, year);

        String expectedMapKey = "EDUCATION total expenses for the period(YYYY/MM) 2022/05";
        BigDecimal expectedTotalValue = BigDecimal.valueOf(30);
        Map<String, BigDecimal> expectedMap = Map.of(expectedMapKey, expectedTotalValue);

        assertEquals(expectedMap, result);
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
