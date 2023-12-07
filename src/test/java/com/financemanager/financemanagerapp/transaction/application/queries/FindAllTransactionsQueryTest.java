package com.financemanager.financemanagerapp.transaction.application.queries;

import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.infra.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FindAllTransactionsQueryTest {

    @Test
    void executeShouldReturnAllTransactions() {
        TransactionRepository mockRepository = Mockito.mock(TransactionRepository.class);
        FindAllTransactionsQuery query = new FindAllTransactionsQuery(mockRepository);

        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction.TransactionBuilder().build(),
                new Transaction.TransactionBuilder().build()
        );

        when(mockRepository.findAll()).thenReturn(expectedTransactions);

        List<Transaction> result = query.execute();

        assertEquals(expectedTransactions, result);
    }
}
