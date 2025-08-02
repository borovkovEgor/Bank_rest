package com.example.bankcards.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(Long cardId, BigDecimal available, BigDecimal requested) {
        super(String.format("Insufficient funds on card %d. Available: %s, Requested: %s", 
                          cardId, available, requested));
    }
    
    public InsufficientFundsException(String cardNumber, BigDecimal available, BigDecimal requested) {
        super(String.format("Insufficient funds on card %s. Available: %s, Requested: %s", 
                          cardNumber, available, requested));
    }
} 