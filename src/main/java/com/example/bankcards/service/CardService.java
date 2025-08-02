package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CardNotActiveException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMaskUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardService {
    
    private final CardRepository cardRepository;
    private final UserService userService;
    
    public CardService(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }
    
    public Card createCard(Long userId, String cardHolder, LocalDate expiryDate) {
        User user = userService.getUserById(userId);

        String cardNumber;
        do {
            cardNumber = CardMaskUtil.generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));
        
        Card card = new Card(cardNumber, cardHolder, expiryDate, user);
        return cardRepository.save(card);
    }
    
    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }
    
    public CardDto getCardDtoById(Long id) {
        Card card = getCardById(id);
        return new CardDto(card);
    }
    
    public Page<CardDto> getUserCards(Long userId, Pageable pageable) {
        Page<Card> cards = cardRepository.findByUserId(userId, pageable);
        return cards.map(CardDto::new);
    }
    
    public Page<CardDto> getAllCards(Pageable pageable) {
        Page<Card> cards = cardRepository.findAll(pageable);
        return cards.map(CardDto::new);
    }
    
    public Page<CardDto> getCardsByStatus(Card.CardStatus status, Pageable pageable) {
        Page<Card> cards = cardRepository.findByStatus(status, pageable);
        return cards.map(CardDto::new);
    }
    
    public Page<CardDto> searchCardsByHolder(String cardHolder, Pageable pageable) {
        Page<Card> cards = cardRepository.findByCardHolderContainingIgnoreCase(cardHolder, pageable);
        return cards.map(CardDto::new);
    }
    
    public List<Card> getActiveCardsByUserId(Long userId) {
        return cardRepository.findActiveCardsByUserId(userId);
    }
    
    public Card blockCard(Long cardId) {
        Card card = getCardById(cardId);
        card.setStatus(Card.CardStatus.BLOCKED);
        return cardRepository.save(card);
    }
    
    public Card activateCard(Long cardId) {
        Card card = getCardById(cardId);
        card.setStatus(Card.CardStatus.ACTIVE);
        return cardRepository.save(card);
    }
    
    public void deleteCard(Long cardId) {
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }
    
    public void updateExpiredCards() {
        List<Card> expiredCards = cardRepository.findExpiredCards();
        for (Card card : expiredCards) {
            card.setStatus(Card.CardStatus.EXPIRED);
            cardRepository.save(card);
        }
    }
    
    public boolean isCardActive(Long cardId) {
        Card card = getCardById(cardId);
        return card.isActive();
    }
    
    public void validateCardForTransaction(Long cardId) {
        Card card = getCardById(cardId);
        if (!card.isActive()) {
            throw new CardNotActiveException(cardId, card.getStatus().name());
        }
    }
    
    public List<CardDto> getUserActiveCards(Long userId) {
        List<Card> cards = getActiveCardsByUserId(userId);
        return cards.stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
    }
} 