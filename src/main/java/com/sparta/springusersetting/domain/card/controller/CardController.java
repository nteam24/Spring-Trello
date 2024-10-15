package com.sparta.springusersetting.domain.card.controller;


import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    private void createCard(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CardRequestDto card)
    {
        cardService.createCard(authUser,card);
    }

    @GetMapping("/{cardId}")
    private void getCard(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long cardId)
    {
        cardService.getCard(authUser,cardId);
    }

    @PatchMapping("/{cardId}")
    private void updateCard(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CardRequestDto card, @PathVariable Long cardId)
    {
        cardService.updateCard(authUser,card,cardId);
    }

    @DeleteMapping("/{cardId}")
    private void deleteCard(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long cardId)
    {
        cardService.deleteCard(authUser,cardId);
    }


}