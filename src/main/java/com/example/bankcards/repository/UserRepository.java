package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Находит пользователя по имени пользователя
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Находит пользователя по email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Проверяет, существует ли пользователь с данным именем
     */
    boolean existsByUsername(String username);
    
    /**
     * Проверяет, существует ли пользователь с данным email
     */
    boolean existsByEmail(String email);
} 