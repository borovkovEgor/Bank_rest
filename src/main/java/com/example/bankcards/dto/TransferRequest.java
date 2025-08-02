package com.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferRequest {
    
    @NotNull(message = "ID карты отправителя обязательно")
    private Long fromCardId;
    
    @NotNull(message = "ID карты получателя обязательно")
    private Long toCardId;
    
    @NotNull(message = "Сумма перевода обязательна")
    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше ")
    private BigDecimal amount;

    public TransferRequest() {}
    
    public TransferRequest(Long fromCardId, Long toCardId, BigDecimal amount) {
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
    }

    public Long getFromCardId() {
        return fromCardId;
    }
    
    public void setFromCardId(Long fromCardId) {
        this.fromCardId = fromCardId;
    }
    
    public Long getToCardId() {
        return toCardId;
    }
    
    public void setToCardId(Long toCardId) {
        this.toCardId = toCardId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
} 