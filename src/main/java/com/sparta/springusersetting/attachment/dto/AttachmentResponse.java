package com.sparta.springusersetting.attachment.dto;

import com.sparta.springusersetting.attachment.entity.Attachment;
import lombok.Getter;

@Getter
public class AttachmentResponse {
    private Long id;
    private String s3Url;

    public AttachmentResponse(Attachment attachment) {
        this.id = attachment.getId();
        this.s3Url = attachment.getS3Url();
    }
}
