package com.sparta.springusersetting.domain.card.controller;


import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    private ResponseEntity<ApiResponse<String>> createCard(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CardRequestDto card)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.createCard(authUser,card)));
    }

    @GetMapping("/{cardId}")
    private ResponseEntity<ApiResponse<CardResponseDto>> getCard(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long cardId)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.getCard(authUser,cardId)));
    }

    @PatchMapping("/{cardId}")
    private  ResponseEntity<ApiResponse<String>>  updateCard(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CardRequestDto card, @PathVariable Long cardId)
    {
        ResponseEntity.ok(ApiResponse.success(cardService.updateCard(authUser,card,cardId)));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<String>> deleteCard(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long cardId)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.deleteCard(authUser,cardId)));
    }



}