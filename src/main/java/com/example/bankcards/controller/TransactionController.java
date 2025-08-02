package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.TransactionService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Транзакции", description = "API для управления транзакциями")
public class TransactionController {
    
    private final TransactionService transactionService;
    private final UserService userService;
    
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Перевод между картами", description = "Перевод денег между картами")
    public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransferRequest request) {
        Transaction transaction = transactionService.transferBetweenCards(
            request.getFromCardId(), request.getToCardId(), request.getAmount()
        );
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить транзакцию по ID", description = "Получить информацию о транзакции по ID")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/my")
    @Operation(summary = "Мои транзакции", description = "Получить все транзакции текущего пользователя")
    public ResponseEntity<Page<Transaction>> getMyTransactions(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        Page<Transaction> transactions = transactionService.getUserTransactions(user.getId(), pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/card/{cardId}")
    @Operation(summary = "Транзакции по карте", description = "Получить все транзакции по указанной карте")
    public ResponseEntity<Page<Transaction>> getCardTransactions(
            @PathVariable Long cardId, Pageable pageable) {
        Page<Transaction> transactions = transactionService.getCardTransactions(cardId, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Транзакции по статусу", description = "Получить транзакции, отфильтрованные по статусу")
    public ResponseEntity<Page<Transaction>> getTransactionsByStatus(
            @Parameter(description = "Transaction status") @PathVariable Transaction.TransactionStatus status,
            Pageable pageable) {
        Page<Transaction> transactions = transactionService.getTransactionsByStatus(status, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/my/completed")
    @Operation(summary = "Мои успешные транзакции", description = "Получить все успешно завершённые транзакции пользователя")
    public ResponseEntity<List<Transaction>> getMyCompletedTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        List<Transaction> transactions = transactionService.getCompletedTransactionsByUserId(user.getId());
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/my/failed")
    @Operation(summary = "Мои неудачные транзакции", description = "Получить все неудачные транзакции пользователя")
    public ResponseEntity<List<Transaction>> getMyFailedTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        List<Transaction> transactions = transactionService.getFailedTransactionsByUserId(user.getId());
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/balance")
    @Operation(summary = "Общий баланс пользователя", description = "Получить суммарный баланс по всем активным картам пользователя")
    public ResponseEntity<BigDecimal> getMyTotalBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        BigDecimal totalBalance = transactionService.getUserTotalBalance(user.getId());
        return ResponseEntity.ok(totalBalance);
    }
} 