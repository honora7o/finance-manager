package com.financemanager.financemanagerapp.domain.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final SaveTransactionCommand createTransactionCommand;
    private final FindAllTransactionsQuery findAllTransactionsQuery;

    public TransactionController(
            SaveTransactionCommand createTransactionCommand,
            FindAllTransactionsQuery findAllTransactionsQuery
    ) {
        this.createTransactionCommand = createTransactionCommand;
        this.findAllTransactionsQuery = findAllTransactionsQuery;
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


}
