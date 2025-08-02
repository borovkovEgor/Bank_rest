package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Card.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    /**
     * Находит карту по номеру
     */
    Optional<Card> findByCardNumber(String cardNumber);
    
    /**
     * Находит все карты пользователя с пагинацией
     */
    Page<Card> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Находит все карты пользователя по статусу
     */
    List<Card> findByUserIdAndStatus(Long userId, CardStatus status);
    
    /**
     * Находит все активные карты пользователя
     */
    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.status = 'ACTIVE' AND c.expiryDate >= CURRENT_DATE")
    List<Card> findActiveCardsByUserId(@Param("userId") Long userId);
    
    /**
     * Находит все карты по статусу с пагинацией
     */
    Page<Card> findByStatus(CardStatus status, Pageable pageable);
    
    /**
     * Находит карты по владельцу (поиск по имени)
     */
    @Query("SELECT c FROM Card c WHERE LOWER(c.cardHolder) LIKE LOWER(CONCAT('%', :cardHolder, '%'))")
    Page<Card> findByCardHolderContainingIgnoreCase(@Param("cardHolder") String cardHolder, Pageable pageable);
    
    /**
     * Проверяет, существует ли карта с данным номером
     */
    boolean existsByCardNumber(String cardNumber);
    
    /**
     * Находит все карты с истекшим сроком действия
     */
    @Query("SELECT c FROM Card c WHERE c.expiryDate < CURRENT_DATE AND c.status = 'ACTIVE'")
    List<Card> findExpiredCards();
} 