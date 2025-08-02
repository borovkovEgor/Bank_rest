package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    
    @Mock
    private CardRepository cardRepository;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private CardService cardService;
    
    private User testUser;
    private Card testCard;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User");
        
        testCard = new Card();
        testCard.setId(1L);
        testCard.setCardNumber("1234567890123456");
        testCard.setCardHolder("Test User");
        testCard.setExpiryDate(LocalDate.now().plusYears(1));
        testCard.setStatus(Card.CardStatus.ACTIVE);
        testCard.setBalance(BigDecimal.valueOf(1000.00));
        testCard.setUser(testUser);
    }
    
    @Test
    void createCard_Success() {
        // Given
        Long userId = 1L;
        String cardHolder = "Test User";
        LocalDate expiryDate = LocalDate.now().plusYears(1);
        
        when(userService.getUserById(userId)).thenReturn(testUser);
        when(cardRepository.existsByCardNumber(any())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        
        // When
        Card result = cardService.createCard(userId, cardHolder, expiryDate);
        
        // Then
        assertNotNull(result);
        assertEquals(cardHolder, result.getCardHolder());
        assertEquals(expiryDate, result.getExpiryDate());
        assertEquals(testUser, result.getUser());
        verify(cardRepository).save(any(Card.class));
    }
    
    @Test
    void getCardById_Success() {
        // Given
        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        
        // When
        Card result = cardService.getCardById(cardId);
        
        // Then
        assertNotNull(result);
        assertEquals(cardId, result.getId());
    }
    
    @Test
    void getCardById_NotFound() {
        // Given
        Long cardId = 999L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(cardId));
    }
    
    @Test
    void blockCard_Success() {
        // Given
        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        
        // When
        Card result = cardService.blockCard(cardId);
        
        // Then
        assertEquals(Card.CardStatus.BLOCKED, result.getStatus());
        verify(cardRepository).save(testCard);
    }
    
    @Test
    void activateCard_Success() {
        // Given
        Long cardId = 1L;
        testCard.setStatus(Card.CardStatus.BLOCKED);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        
        // When
        Card result = cardService.activateCard(cardId);
        
        // Then
        assertEquals(Card.CardStatus.ACTIVE, result.getStatus());
        verify(cardRepository).save(testCard);
    }
    
    @Test
    void isCardActive_True() {
        // Given
        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        
        // When
        boolean result = cardService.isCardActive(cardId);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isCardActive_False() {
        // Given
        Long cardId = 1L;
        testCard.setStatus(Card.CardStatus.BLOCKED);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        
        // When
        boolean result = cardService.isCardActive(cardId);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void getActiveCardsByUserId_Success() {
        // Given
        Long userId = 1L;
        List<Card> activeCards = Arrays.asList(testCard);
        when(cardRepository.findActiveCardsByUserId(userId)).thenReturn(activeCards);
        
        // When
        List<Card> result = cardService.getActiveCardsByUserId(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard, result.get(0));
    }
} 