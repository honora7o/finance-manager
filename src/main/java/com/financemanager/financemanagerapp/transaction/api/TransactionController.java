package com.financemanager.financemanagerapp.transaction.api;

import com.financemanager.financemanagerapp.transaction.application.commands.RegisterTransactionCommand;
import com.financemanager.financemanagerapp.transaction.application.queries.FindAllTransactionsQuery;
import com.financemanager.financemanagerapp.transaction.application.queries.FindMonthlyExpensesTotalByCategoryQuery;
import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import com.financemanager.financemanagerapp.transaction.domain.TransactionCategoryEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final RegisterTransactionCommand createTransactionCommand;
    private final FindAllTransactionsQuery findAllTransactionsQuery;
    private final FindMonthlyExpensesTotalByCategoryQuery findMonthlyExpensesTotalByCategoryQuery;

    public TransactionController(
            RegisterTransactionCommand createTransactionCommand,
            FindAllTransactionsQuery findAllTransactionsQuery,
            FindMonthlyExpensesTotalByCategoryQuery findMonthlyExpensesTotalByCategoryQuery
    ) {
        this.createTransactionCommand = createTransactionCommand;
        this.findAllTransactionsQuery = findAllTransactionsQuery;
        this.findMonthlyExpensesTotalByCategoryQuery = findMonthlyExpensesTotalByCategoryQuery;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Transaction transaction) {
       this.createTransactionCommand.execute(transaction);
       return ResponseEntity.created(java.net.URI.create("/api/transactions")).build();
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll() {
        var result = findAllTransactionsQuery.execute();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/monthly-expenses")
    public ResponseEntity<Map<String, BigDecimal>> findMonthlyExpensesTotalByCat(
            @RequestParam(name = "category") TransactionCategoryEnum category,
            @RequestParam(name = "month", required = false) Optional<Integer> month,
            @RequestParam(name = "year", required = false) Optional<Integer> year
    ) {
        var result = findMonthlyExpensesTotalByCategoryQuery.execute(category, month, year);
        return ResponseEntity.ok(result);
    }
}
