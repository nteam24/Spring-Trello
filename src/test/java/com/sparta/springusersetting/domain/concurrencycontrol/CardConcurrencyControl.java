//package com.sparta.springusersetting.domain.concurrencycontrol;
//
//
//import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
//import com.sparta.springusersetting.domain.card.entity.Card;
//import com.sparta.springusersetting.domain.card.repository.CardRepository;
//import com.sparta.springusersetting.domain.user.entity.User;
//import com.sparta.springusersetting.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import static com.sparta.springusersetting.domain.user.enums.UserRole.ROLE_ADMIN;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class CardConcurrencyControl {
//
//    @Autowired
//    private CardRepository cardRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private Card card;
//
//    @BeforeEach
//    public void setup() {
//        User user = userRepository.save(new User("user1@example.com", "password123","황근출",ROLE_ADMIN));
//        card = new Card();
//        ReflectionTestUtils.setField(card,"title", "test title");
//        ReflectionTestUtils.setField(card, "contents", "test title2");
//        ReflectionTestUtils.setField(card, "deadline", LocalDate.now().plusDays(1));
//        cardRepository.save(card);
//    }
//
//    @Test
//    @Transactional
//    public void testConcurrentCardUpdates() throws Exception {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        // Define two update requests
//        CardRequestDto requestDto1 = new CardRequestDto(1L, 1L, "Updated Title 1", "Updated Contents 1", LocalDate.now().plusDays(2));
//        CardRequestDto requestDto2 = new CardRequestDto(2L, 2L, "Updated Title 2", "Updated Contents 2", LocalDate.now().plusDays(3));
//
//        // Simulate two concurrent update tasks
//        Future<?> future1 = executor.submit(() -> {
//            updateCard(requestDto1);
//        });
//
//        Future<?> future2 = executor.submit(() -> {
//            updateCard(requestDto2);
//        });
//
//        future1.get();
//        future2.get();
//
//        // Validate that only one of them was successful
//        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
//
//        assertTrue(updatedCard.getTitle().equals("Updated Title 1") || updatedCard.getTitle().equals("Updated Title 2"));
//    }
//
//    private void updateCard(CardRequestDto requestDto) {
//        Card cardToUpdate = cardRepository.findById(requestDto.getListId()).orElseThrow();
//        cardToUpdate.update(null, null, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
//        cardRepository.save(cardToUpdate);
//    }
//}