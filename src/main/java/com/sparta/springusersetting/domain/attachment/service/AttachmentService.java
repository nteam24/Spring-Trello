package com.sparta.springusersetting.domain.attachment.service;

import com.sparta.springusersetting.domain.attachment.dto.AttachmentResponse;
import com.sparta.springusersetting.domain.attachment.entity.Attachment;
import com.sparta.springusersetting.domain.attachment.exception.FileFormatException;
import com.sparta.springusersetting.domain.attachment.exception.FileSizeException;
import com.sparta.springusersetting.domain.attachment.repository.AttachmentRepository;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.exception.NotFoundCardException;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.common.service.S3Service;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
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
@Transactional(readOnly = true)
public class AttachmentService {

    private final CardRepository cardRepository;
    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;
    private final MemberManageService memberManageService;
    private final UserRepository userRepository;

    // 지원되는 파일 형식과 크기 제한
    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf", "text/csv");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // 파일 저장
    @Transactional
    public AttachmentResponse saveFile(AuthUser authUser, Long cardId, MultipartFile file) throws IOException {
        User createUser = userRepository.findById(authUser.getUserId()).orElseThrow(NotFoundUserException::new);
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        if(memberManageService.checkMemberRole(createUser.getId(),card.getLists().getBoard().getWorkspace().getId()) == MemberRole.ROLE_READ_USER)
        {
            throw new BadAccessUserException();
        }


        // 파일 형식 및 크기 검사
        if (!SUPPORTED_FILE_TYPES.contains(file.getContentType())) {
            throw new FileFormatException();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeException();
        }

        // 파일 저장
        String fileUrl = s3Service.uploadFile(file);
        Attachment attachment = new Attachment(fileUrl, card);
        attachmentRepository.save(attachment);
        AttachmentResponse response = new AttachmentResponse(attachment);
        return response;
    }

    // 첨부파일 조회
    public List<AttachmentResponse> getFiles(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(NotFoundCardException::new);;

        List<Attachment> attachments = attachmentRepository.findAllByCard(card);
        List<AttachmentResponse> response = attachments.stream()
                .map(AttachmentResponse::new)
                .collect(Collectors.toList());

        return response;
    }

    // 첨부파일 삭제
    @Transactional
    public Void deleteFile(AuthUser authUser, Long cardId, Long fileId) {
        User createUser = userRepository.findById(authUser.getUserId()).orElseThrow(NotFoundUserException::new);
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        if(memberManageService.checkMemberRole(createUser.getId(),card.getLists().getBoard().getWorkspace().getId()) == MemberRole.ROLE_READ_USER)
        {
            throw new BadAccessUserException();
        }

        if (!cardRepository.existsById(cardId)) {
            throw new NotFoundCardException();
        }
        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(NotFoundFileException::new);

        attachmentRepository.delete(attachment);
        return null;
    }
}
