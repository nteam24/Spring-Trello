package com.sparta.springusersetting.attachment.repository;

import com.sparta.springusersetting.attachment.entity.Attachment;
import com.sparta.springusersetting.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCard(Card card);
}
