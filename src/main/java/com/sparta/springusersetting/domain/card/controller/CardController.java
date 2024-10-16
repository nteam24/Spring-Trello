package com.sparta.springusersetting.domain.card.controller;


import com.sparta.springusersetting.attachment.service.AttachmentService;
import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createCard(@AuthenticationPrincipal AuthUser authUser,
                                                          @Valid @RequestBody CardRequestDto card,
                                                          @RequestPart(name = "file", required = false) MultipartFile file)

    {

        return ResponseEntity.ok(ApiResponse.success(cardService.createCard(authUser,card,file)));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardResponseDto>> getCard(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long cardId)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.getCard(authUser,cardId)));
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<ApiResponse<String>> updateCard(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CardRequestDto card, @PathVariable Long cardId)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.updateCard(authUser,card,cardId)));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<String>> deleteCard(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long cardId)
    {
        return ResponseEntity.ok(ApiResponse.success(cardService.deleteCard(authUser,cardId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CardSearchResponseDto>>> searchCard(@AuthenticationPrincipal AuthUser authUser,
                                                                               @ModelAttribute CardSearchRequestDto searchRequest,
                                                                               Pageable pageable){
        Page<CardSearchResponseDto> result = cardService.searchCard(authUser.getUserId(), searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
