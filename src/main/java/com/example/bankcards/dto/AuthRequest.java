package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    
    @NotBlank(message = "Имя пользователя обязательно")
    private String username;
    
    @NotBlank(message = "Пароль обязателен")
    private String password;

    public AuthRequest() {}
    
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
} 