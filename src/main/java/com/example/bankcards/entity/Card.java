package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Номер карты обязателен")
    @Size(min = 16, max = 16, message = "Номер карты должен содержать ровно 16 цифр")
    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;
    
    @NotBlank(message = "Имя владельца карты обязательно")
    @Column(name = "card_holder", nullable = false)
    private String cardHolder;
    
    @NotNull(message = "Дата окончания действия карты обязательна")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status = CardStatus.ACTIVE;
    
    @DecimalMin(value = "0.0", message = "Баланс не может быть отрицательным")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Card() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Card(String cardNumber, String cardHolder, LocalDate expiryDate, User user) {
        this();
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCardHolder() {
        return cardHolder;
    }
    
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public CardStatus getStatus() {
        return status;
    }
    
    public void setStatus(CardStatus status) {
        this.status = status;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public boolean isActive() {
        return status == CardStatus.ACTIVE && !isExpired();
    }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
    
    public boolean canTransfer(BigDecimal amount) {
        return isActive() && balance.compareTo(amount) >= 0;
    }
    
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void subtractBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum CardStatus {
        ACTIVE, BLOCKED, EXPIRED
    }
} 