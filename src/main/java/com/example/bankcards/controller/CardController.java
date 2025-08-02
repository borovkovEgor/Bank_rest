package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Карты", description = "API для управления банковскими картами")
public class CardController {
    
    private final CardService cardService;
    private final UserService userService;
    
    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новую карту", description = "Создание новой банковской карты для пользователя")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CreateCardRequest request) {
        Card card = cardService.createCard(request.getUserId(), request.getCardHolder(), request.getExpiryDate());
        return ResponseEntity.ok(new CardDto(card));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить карту по ID", description = "Получение информации о карте по её ID")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        CardDto card = cardService.getCardDtoById(id);
        return ResponseEntity.ok(card);
    }
    
    @GetMapping("/my")
    @Operation(summary = "Мои карты", description = "Получить все карты текущего аутентифицированного пользователя")
    public ResponseEntity<Page<CardDto>> getMyCards(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        Page<CardDto> cards = cardService.getUserCards(user.getId(), pageable);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/my/active")
    @Operation(summary = "Активные карты", description = "Получить все активные карты текущего пользователя")
    public ResponseEntity<List<CardDto>> getMyActiveCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        List<CardDto> cards = cardService.getUserActiveCards(user.getId());
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Все карты", description = "Получить все карты с пагинацией")
    public ResponseEntity<Page<CardDto>> getAllCards(Pageable pageable) {
        Page<CardDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Карты по статусу", description = "Фильтрация карт по статусу")
    public ResponseEntity<Page<CardDto>> getCardsByStatus(
            @Parameter(description = "Card status") @PathVariable Card.CardStatus status,
            Pageable pageable) {
        Page<CardDto> cards = cardService.getCardsByStatus(status, pageable);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Поиск по владельцу", description = "Поиск карт по имени владельца")
    public ResponseEntity<Page<CardDto>> searchCardsByHolder(
            @Parameter(description = "Card holder name") @RequestParam String cardHolder,
            Pageable pageable) {
        Page<CardDto> cards = cardService.searchCardsByHolder(cardHolder, pageable);
        return ResponseEntity.ok(cards);
    }
    
    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Заблокировать карту", description = "Блокировка карты по ID")
    public ResponseEntity<CardDto> blockCard(@PathVariable Long id) {
        Card card = cardService.blockCard(id);
        return ResponseEntity.ok(new CardDto(card));
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Активировать карту", description = "Активация ранее заблокированной карты")
    public ResponseEntity<CardDto> activateCard(@PathVariable Long id) {
        Card card = cardService.activateCard(id);
        return ResponseEntity.ok(new CardDto(card));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить карту", description = "Удаление карты по ID")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/request-block")
    @Operation(summary = "Запрос на блокировку карты", description = "Пользователь может запросить блокировку своей карты")
    public ResponseEntity<CardDto> requestCardBlock(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        
        CardDto card = cardService.getCardDtoById(id);
        if (!card.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Card blockedCard = cardService.blockCard(id);
        return ResponseEntity.ok(new CardDto(blockedCard));
    }
} 