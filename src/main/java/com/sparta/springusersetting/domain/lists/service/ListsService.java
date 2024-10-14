package com.sparta.springusersetting.domain.lists.service;

import com.sparta.springusersetting.domain.lists.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListsService {
    private final ListRepository listRepository;
}
