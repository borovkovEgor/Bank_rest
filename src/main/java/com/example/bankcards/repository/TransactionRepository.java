package com.example.bankcards.repository;

import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.Transaction.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Находит все транзакции пользователя (по картам пользователя)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromCard.user.id = :userId OR t.toCard.user.id = :userId")
    Page<Transaction> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Находит все транзакции по статусу
     */
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);
    
    /**
     * Находит все транзакции карты (исходящие и входящие)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromCard.id = :cardId OR t.toCard.id = :cardId")
    Page<Transaction> findByCardId(@Param("cardId") Long cardId, Pageable pageable);
    
    /**
     * Находит все транзакции за период
     */
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    Page<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate, 
                                     Pageable pageable);
    
    /**
     * Находит все завершенные транзакции пользователя
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromCard.user.id = :userId OR t.toCard.user.id = :userId) AND t.status = 'COMPLETED'")
    List<Transaction> findCompletedTransactionsByUserId(@Param("userId") Long userId);
    
    /**
     * Находит все неудачные транзакции пользователя
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromCard.user.id = :userId OR t.toCard.user.id = :userId) AND t.status = 'FAILED'")
    List<Transaction> findFailedTransactionsByUserId(@Param("userId") Long userId);
} 