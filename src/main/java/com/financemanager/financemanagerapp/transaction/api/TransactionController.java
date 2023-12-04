package com.financemanager.financemanagerapp.transaction.api;

import com.financemanager.financemanagerapp.transaction.application.commands.RegisterTransactionCommand;
import com.financemanager.financemanagerapp.transaction.application.queries.FindAllTransactionsQuery;
import com.financemanager.financemanagerapp.transaction.domain.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final RegisterTransactionCommand createTransactionCommand;
    private final FindAllTransactionsQuery findAllTransactionsQuery;

    public TransactionController(
            RegisterTransactionCommand createTransactionCommand,
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
