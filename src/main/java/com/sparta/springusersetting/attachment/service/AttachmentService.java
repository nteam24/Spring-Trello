package com.sparta.springusersetting.attachment.service;

import com.sparta.springusersetting.attachment.dto.AttachmentResponse;
import com.sparta.springusersetting.attachment.entity.Attachment;
import com.sparta.springusersetting.attachment.repository.AttachmentRepository;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final CardRepository cardRepository;
    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;

    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf", "text/csv");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    public AttachmentResponse saveFile(Long cardId, MultipartFile file) throws IOException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        // 파일 형식 및 크기 검사
        if (!SUPPORTED_FILE_TYPES.contains(file.getContentType())) {
            throw new IOException();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException();
        }

        // 파일 저장
        String fileUrl = s3Service.uploadFile(file);
        Attachment attachment = new Attachment(fileUrl, card);
        attachmentRepository.save(attachment);
        AttachmentResponse response = new AttachmentResponse(attachment);
        return response;
    }
    // 첨부파일 삭제
    @Transactional
    public void deleteFile(Long cardId, Long fileId) {
        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }
        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 파일 ID 입니다."));

        attachmentRepository.delete(attachment);


    }

    // 첨부파일 조회
    public List<AttachmentResponse> getFiles(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        List<Attachment> attachments = attachmentRepository.findAllByCard(card);
        List<AttachmentResponse> response = attachments.stream()
                .map(AttachmentResponse::new)
                .toList();
        return response;
    }




}