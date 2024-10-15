package com.sparta.springusersetting.attachment.repository;

import com.sparta.springusersetting.attachment.entity.Attachment;
import com.sparta.springusersetting.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCard(Card card);
}
