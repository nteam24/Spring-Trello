package com.sparta.springusersetting.domain.card.service;


import com.sparta.springusersetting.domain.card.dto.ActivityLogResponseDto;
import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.exception.NotFoundCardException;
import com.sparta.springusersetting.domain.card.repository.ActivityLogRepository;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.repository.ListsRepository;
import com.sparta.springusersetting.domain.lists.service.ListsService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserService userService;
    private final ListsRepository listsRepository;
    private final ActivityLogRepository activityLogRepository;


    public void createCard(AuthUser authUser, CardRequestDto requestDto)
    {
        Lists lists = listsRepository.findById(requestDto.getListId()).orElse(null);
        User createUser = User.fromAuthUser(authUser);


       // if(lists.getBoard().getWorkspace().getMemberRole()==MemberRole.ROLE_READ_USER)
        User manager = userService.findUser(requestDto.getManagerId());
        Card card = new Card(requestDto.getTitle(),requestDto.getContents(),requestDto.getDeadline(),manager,lists);
        cardRepository.save(card);
    }

    public CardResponseDto getCard(AuthUser authUser, Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        CardResponseDto cardResponseDto = new CardResponseDto(
                card.getId(),
                card.getTitle(),
                card.getContents(),
                card.getDeadline(),
                card.getManager().getEmail(),
                card.getActivityLogs().stream().map(ActivityLogResponseDto::new).toList(),
                card.getComments());
        return cardResponseDto;
    }


    @Transactional
    public void updateCard(AuthUser authUser, CardRequestDto requestDto, Long cardId)
    {
        User manager = userService.findUser(requestDto.getManagerId());
        Lists lists = listsRepository.findById(requestDto.getListId()).orElse(null);
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        card.update(manager,lists,requestDto.getTitle(),requestDto.getContents(),requestDto.getDeadline());

        cardRepository.save(card);
    }

    public void deleteCard(AuthUser authUser, Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        cardRepository.delete(card);
    }

    public Card findCard(AuthUser authUser, Long cardId)
    {
        Card card = cardRepository.findById(cardId).orElseThrow(NotFoundCardException::new);
        return card;

    }

}
