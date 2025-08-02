package com.example.bankcards.util;

public class CardMaskUtil {
    
    private CardMaskUtil() {
    }
    
    /**
     * Маскирует номер карты, оставляя только последние 4 цифры
     * Формат: **** **** **** 1234
     */
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
    
    /**
     * Генерирует случайный номер карты (16 цифр)
     */
    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        
        return cardNumber.toString();
    }
    
    /**
     * Проверяет, является ли строка валидным номером карты
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        return cardNumber.matches("\\d{16}");
    }
} 