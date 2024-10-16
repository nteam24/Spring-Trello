package com.sparta.springusersetting.attachment.controller;

import com.sparta.springusersetting.attachment.dto.AttachmentResponse;
import com.sparta.springusersetting.attachment.service.AttachmentService;
import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cards/{cardId}/file   s")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;


    // 첨부파일 추가
    @PutMapping
    public ResponseEntity<ApiResponse<AttachmentResponse>> saveFiles(@AuthenticationPrincipal AuthUser authUser,
                                                                     @PathVariable Long cardId,
                                                                     @RequestPart(name = "file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(attachmentService.saveFile(authUser, cardId, file)));
    }

    // 첨부파일 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getFiles(
            @PathVariable Long cardId) {
        return ResponseEntity.ok(ApiResponse.success(attachmentService.getFiles(cardId)));
    }

    // 첨부파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@AuthenticationPrincipal AuthUser authUser,
                                                        @PathVariable Long cardId,
                                                        @PathVariable Long fileId) {
        return ResponseEntity.ok(ApiResponse.success(attachmentService.deleteFile(authUser, cardId, fileId)));
    }
}
