package com.sparta.springusersetting.domain.card.service;


import com.sparta.springusersetting.domain.attachment.exception.FileSaveException;
import com.sparta.springusersetting.domain.attachment.service.AttachmentService;
import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import com.sparta.springusersetting.domain.card.dto.CardWithViewCountResponseDto;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.exception.BadAccessCardException;
import com.sparta.springusersetting.domain.card.exception.CrashUpdateCardsException;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.exception.NotFoundListsException;
import com.sparta.springusersetting.domain.lists.repository.ListsRepository;
import com.sparta.springusersetting.domain.notification.util.NotificationUtil;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sparta.springusersetting.domain.notification.enums.NotificationConst.UPDATE_CARD_NOTIFICATION;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final ListsRepository listsRepository;
    private final UserService userService;
    private final MemberManageService memberManageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationUtil notificationUtil;
    private final AttachmentService attachmentService;

    private static final String TODAY_RANKING_KEY = "todayCardRanking";

    public static Long previousViewCount = 0L;


    @Transactional
    public String createCard(AuthUser authUser, CardRequestDto requestDto, MultipartFile file)
    {
        Lists lists = listsRepository.findById(requestDto.getListId()).orElseThrow(NotFoundListsException::new);
        User createUser = User.fromAuthUser(authUser);
        if(memberManageService.checkMemberRole(createUser.getId(),lists.getBoard().getWorkspace().getId()) == MemberRole.ROLE_READ_USER)
        {
            throw new BadAccessUserException();
        }

        User manager = userService.findUser(requestDto.getManagerId());
        Card card = new Card(manager, lists, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        cardRepository.save(card);
        if(file != null && !file.isEmpty()) {
            try {
                attachmentService.saveFile(authUser, card.getId(), file);
            } catch (IOException e)
            {
                throw new FileSaveException();
            }
        }
        return "카드 생성이 완료되었습니다.";
    }

    // 카드 단건 조회
    @Transactional(readOnly = true)
    public CardWithViewCountResponseDto getCard(AuthUser authUser, Long cardId) {
        // 유저 조회
        User user = userService.findUser(authUser.getUserId());

        // 카드 조회
        Card card = findCard(cardId);

        // Redis Set 의 Key 설정
        String todayCardViewSetKey = "todayCardViewSet:" + cardId;

        // 유저 ID를 Set 에 추가하고, 반환된 값으로 추가 성공 여부 확인
        redisTemplate.opsForSet().add(todayCardViewSetKey, user.getId().toString());

        // 조회수를 Set 의 크기로 계산
        Long todayCardViewCount = redisTemplate.opsForSet().size(todayCardViewSetKey);

        // 가장 인기 있는 Top3 카드 업데이트
        updateTopCardTitles(cardId);

        // Dto 반환
        return new CardWithViewCountResponseDto(card, todayCardViewCount);
    }

    // 인기 카드 Top3 조회 ( Redis )
    @Transactional(readOnly = true)
    public Map<String, Integer> getTopViewCardList() {
        Set<ZSetOperations.TypedTuple<Object>> topCardsWithScores =
                redisTemplate.opsForZSet().reverseRangeWithScores(TODAY_RANKING_KEY, 0, 2);

        return topCardsWithScores.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.getValue().toString(),  // title
                        tuple -> tuple.getScore().intValue()    // 조회수 ( score )
                ));
    }


    @Transactional
    public String updateCard(AuthUser authUser, CardRequestDto requestDto, Long cardId, User user) throws IOException {

        Lists lists = listsRepository.findById(requestDto.getListId()).orElseThrow(NotFoundListsException::new);
        User createUser = User.fromAuthUser(authUser);

        if(memberManageService.checkMemberRole(createUser.getId(),lists.getBoard().getWorkspace().getId()) == MemberRole.ROLE_READ_USER)
        {
            throw new BadAccessUserException();
        }

        User manager = userService.findUser(requestDto.getManagerId());

        Card card = findCard(cardId);
        card.update(manager,lists,requestDto.getTitle(),requestDto.getContents(),requestDto.getDeadline());
        cardRepository.save(card);

        // 업데이트 전, 현재 버전과 요청된 버전을 체크
        try {
            card.update(manager, lists, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
            cardRepository.save(card);
        } catch (OptimisticLockingFailureException e) {
            throw new CrashUpdateCardsException(); // 사용자에게 보여줄 메시지
        }

        // 카드 변경 알림 전송
        notificationUtil.sendNotification(
                UPDATE_CARD_NOTIFICATION.getMessage(),
                user.getUserName(),
                card.getTitle(),
                card.getContents(),
                card.getDeadline()
        );

        return "카드 수정이 완료되었습니다.";
    }


    @Transactional
    public String deleteCard(AuthUser authUser, Long cardId) {
        User deletedUser = User.fromAuthUser(authUser);
        Card card = findCard(cardId);
        if(memberManageService.checkMemberRole(deletedUser.getId(),card.getLists().getBoard().getWorkspace().getId()) == MemberRole.ROLE_READ_USER)
        {
            throw new BadAccessUserException();
        }

        cardRepository.delete(card);
        return "카드 삭제가 완료되었습니다.";
    }

    @Transactional(readOnly = true)
    public Slice<CardSearchResponseDto> searchCard(Long userId, CardSearchRequestDto searchRequest, Long cursorId, int pageSize) {
        User user = userService.findUser(userId);

        if (searchRequest.getWorkspaceId() == null) {
            throw new BadAccessCardException();
        }

        // 사용자가 해당 워크스페이스에 속해 있는지 확인
        MemberRole memberRole = memberManageService.checkMemberRole(userId, searchRequest.getWorkspaceId());
        if (memberRole == null) {
            throw new BadAccessCardException();
        }

        // QueryDSL을 사용한 검색 수행
        return cardRepository.searchCards(searchRequest, cursorId, pageSize);
    }

    // Card DB 에서 조회
    public Card findCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(null);
        return card;
    }

    // Card DB 에 저장
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    // Top3 카드 업데이트 메서드
    private void updateTopCardTitles(Long cardId) {
        // Key : cardViewSet{cardId}
        String todayCardViewSetKey = "todayCardViewSet:" + cardId;

        // 현재 카드의 조회수 가져오기
        Long viewCount = redisTemplate.opsForSet().size(todayCardViewSetKey);

        // 조회수가 증가했는지 확인
        if (viewCount > previousViewCount) {
            String snapshotKey = "card:snapshot(1h):" + cardId;
            redisTemplate.opsForValue().increment(snapshotKey, 1); // snapshotKey 값을 1 증가
        }

        previousViewCount = viewCount; // 이전 조회수 업데이트

        // 카드 제목 가져오기 (Redis 사용)
        String cardTitle = getCardTitle(cardId);

        // 카드 제목과 조회수를 Sorted Set 에 추가
        redisTemplate.opsForZSet().add(TODAY_RANKING_KEY, cardTitle, viewCount);

        // 상위 3개 카드 제목만 유지
        maintainTopCards();
    }

    // Top 카드 제목 가져오는 메서드
    private void maintainTopCards() {
        // 전체 카드 수 가져오기
        Long size = redisTemplate.opsForZSet().zCard(TODAY_RANKING_KEY);

        // 카드가 3개 이하라면 아무것도 지우지 않음
        if (size == null || size <= 3) {
            return;
        }

        // 상위 3개를 제외한 나머지 카드 제거
        redisTemplate.opsForZSet().removeRange(TODAY_RANKING_KEY, 0, size - 4);
    }

    // 카드 제목을 Redis 에서 가져오고, 없으면 데이터베이스에서 조회 후 Redis 에 저장
    private String getCardTitle(Long cardId) {
        String cardTitleKey = "cardTitle:" + cardId;
        String title = (String) redisTemplate.opsForValue().get(cardTitleKey);

        if (title == null) {
            Card card = findCard(cardId);
            title = card.getTitle();
            // Redis 에 캐시
            redisTemplate.opsForValue().set(cardTitleKey, title);
        }

        return title;
    }
}
