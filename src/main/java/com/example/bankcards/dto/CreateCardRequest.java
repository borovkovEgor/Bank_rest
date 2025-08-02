package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class CreateCardRequest {
    
    @NotNull(message = "ID пользователя обязательно")
    private Long userId;
    
    @NotNull(message = "Не указано имя владельца карты")
    private String cardHolder;
    
    @NotNull(message = "Не указана дата истечения срока действия")
    private LocalDate expiryDate;

    public CreateCardRequest() {}
    
    public CreateCardRequest(Long userId, String cardHolder, LocalDate expiryDate) {
        this.userId = userId;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
} 