package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardMaskUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDto {
    
    private Long id;
    private String maskedCardNumber;
    private String cardHolder;
    private LocalDate expiryDate;
    private String status;
    private BigDecimal balance;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CardDto() {}
    
    public CardDto(Card card) {
        this.id = card.getId();
        this.maskedCardNumber = CardMaskUtil.maskCardNumber(card.getCardNumber());
        this.cardHolder = card.getCardHolder();
        this.expiryDate = card.getExpiryDate();
        this.status = card.getStatus().name();
        this.balance = card.getBalance();
        this.userId = card.getUser().getId();
        this.username = card.getUser().getUsername();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }
    
    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
} 