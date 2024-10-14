package com.sparta.springusersetting.domain.list.service;

import com.sparta.springusersetting.domain.list.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListService {
    private final ListRepository listRepository;
}
