package com.example.bankcards.exception;

public class CardNotFoundException extends RuntimeException {
    
    public CardNotFoundException(String message) {
        super(message);
    }
    
    public CardNotFoundException(Long cardId) {
        super("Card not found with id: " + cardId);
    }
    
    public CardNotFoundException(String cardNumber, String reason) {
        super("Card not found with number: " + cardNumber + ". Reason: " + reason);
    }
} 