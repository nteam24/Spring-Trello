package com.sparta.springusersetting.domain.card.repository;


import com.sparta.springusersetting.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {

}
