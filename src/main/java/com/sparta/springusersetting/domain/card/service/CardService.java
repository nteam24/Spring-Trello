package com.sparta.springusersetting.domain.card.service;


import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    //private final ListRepository listRepository;

    public void createCard(CardRequestDto requestDto)
    {
        User manager = userRepository.findById(requestDto.getManagerId()).orElse(null);
        Card card = new Card(requestDto.getTitle(),requestDto.getContents(),requestDto.getDeadline(),requestDto.get);

    }

    public void getCard() {

    }

    public void updateCard() {
    }

    public void deleteCard() {
    }
}
