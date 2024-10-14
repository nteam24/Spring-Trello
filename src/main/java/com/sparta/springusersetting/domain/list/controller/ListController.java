package com.sparta.springusersetting.domain.list.controller;

import com.sparta.springusersetting.domain.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListController {
    private final ListService listService;
}
