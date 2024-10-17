package com.sparta.springusersetting.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationConst {
    LOGIN_NOTIFICATION("난 %s핑! 코딩빼곤 다 재밌어! "),
    ACCEPT_MEMBER_NOTIFICATION(" %s 님이 카드 수정 및 변경을 하였습니다. 제목:%s, 내용:%s , 마감일:%s"),
    UPDATE_CARD_NOTIFICATION(" %s 님이 %s 님의 카드에 댓글을 남겼습니다. 내용:%s, %s "),
    CREATE_COMMENT_NOTIFICATION("%s 님이 %s 워크스페이스에 참여하였습니다.");

    private final String message;
}
