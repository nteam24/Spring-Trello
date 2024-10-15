package com.sparta.springusersetting.attachment.controller;

import com.sparta.springusersetting.attachment.dto.AttachmentResponse;
import com.sparta.springusersetting.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards/{cardId}/attach")
public class AttachmentController {


    private final AttachmentService attachmentService;

    // 첨부파일 추가
    @PutMapping
    public AttachmentResponse saveFiles(
            @PathVariable Long cardId,
            @RequestPart(name = "file") MultipartFile file) throws IOException
    {
        return attachmentService.saveFile(cardId, file);
    }

    // 첨부파일 조회
    @GetMapping
    public List<AttachmentResponse> getFiles(
            @PathVariable Long cardId) {
        return attachmentService.getFiles(cardId);
    }

    // 첨부파일 삭제
    @DeleteMapping("/{fileId}")
    public void deleteFile(
            @PathVariable Long cardId,
            @PathVariable Long fileId) {
        attachmentService.deleteFile(cardId, fileId);
    }
}
