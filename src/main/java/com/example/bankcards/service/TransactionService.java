package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CardNotActiveException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    
    public TransactionService(TransactionRepository transactionRepository, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
    }
    
    public Transaction transferBetweenCards(Long fromCardId, Long toCardId, BigDecimal amount) {
        Card fromCard = cardService.getCardById(fromCardId);
        Card toCard = cardService.getCardById(toCardId);

        if (!fromCard.getUser().getId().equals(toCard.getUser().getId())) {
            throw new RuntimeException("Transfer is only allowed between cards of the same user");
        }

        if (!fromCard.isActive()) {
            throw new CardNotActiveException(fromCardId, fromCard.getStatus().name());
        }
        if (!toCard.isActive()) {
            throw new CardNotActiveException(toCardId, toCard.getStatus().name());
        }
        
        // Проверяем достаточность средств
        if (!fromCard.canTransfer(amount)) {
            throw new InsufficientFundsException(fromCardId, fromCard.getBalance(), amount);
        }
        
        // Создаем транзакцию
        Transaction transaction = new Transaction(fromCard, toCard, amount);
        transaction = transactionRepository.save(transaction);
        
        try {
            fromCard.subtractBalance(amount);
            toCard.addBalance(amount);

            transaction.markAsCompleted();
            transactionRepository.save(transaction);
            
        } catch (Exception e) {
            transaction.markAsFailed("Transfer failed: " + e.getMessage());
            transactionRepository.save(transaction);
            throw e;
        }
        
        return transaction;
    }
    
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }
    
    public Page<Transaction> getUserTransactions(Long userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable);
    }
    
    public Page<Transaction> getCardTransactions(Long cardId, Pageable pageable) {
        return transactionRepository.findByCardId(cardId, pageable);
    }
    
    public Page<Transaction> getTransactionsByStatus(Transaction.TransactionStatus status, Pageable pageable) {
        return transactionRepository.findByStatus(status, pageable);
    }
    
    public List<Transaction> getCompletedTransactionsByUserId(Long userId) {
        return transactionRepository.findCompletedTransactionsByUserId(userId);
    }
    
    public List<Transaction> getFailedTransactionsByUserId(Long userId) {
        return transactionRepository.findFailedTransactionsByUserId(userId);
    }
    
    public BigDecimal getUserTotalBalance(Long userId) {
        List<Card> activeCards = cardService.getActiveCardsByUserId(userId);
        return activeCards.stream()
                .map(Card::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 