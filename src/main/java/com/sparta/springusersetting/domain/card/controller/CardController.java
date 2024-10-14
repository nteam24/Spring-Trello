package com.sparta.springusersetting.domain.card.controller;


import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    private void createCard(@Valid @RequestBody CardRequestDto card)
    {
        cardService.createCard(card);
    }

    @GetMapping("/{cardId}")
    private CardResponseDto getCard(@PathVariable Long cardId)
    {
        cardService.getCard();
    }

    @PatchMapping
    private void updateCard(@Valid @RequestBody CardRequestDto card)
    {
        cardService.updateCard();
    }

    @DeleteMapping
    private void deleteCard(@Valid @RequestBody CardRequestDto card)
    {
        cardService.deleteCard();
    }


}
