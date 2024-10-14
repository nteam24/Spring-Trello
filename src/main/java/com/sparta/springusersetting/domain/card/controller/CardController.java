package com.sparta.springusersetting.domain.card.controller;


import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    private void createCard(AuthUser authUser, @Valid @RequestBody CardRequestDto card)
    {
        cardService.createCard(authUser,card);
    }

    @GetMapping("/{cardId}")
    private CardResponseDto getCard(AuthUser authUser, @PathVariable Long cardId)
    {
        cardService.getCard(authUser,cardId);
    }

    @PatchMapping("/{cardId}")
    private void updateCard(AuthUser authUser, @Valid @RequestBody CardRequestDto card, @PathVariable Long cardId)
    {
        cardService.updateCard(authUser,cardId,card);
    }

    @DeleteMapping("/{cardId}")
    private void deleteCard(AuthUser authUser,@PathVariable Long cardId, @Valid @RequestBody CardRequestDto card)
    {
        cardService.deleteCard(authUser,cardId,card);
    }


}
