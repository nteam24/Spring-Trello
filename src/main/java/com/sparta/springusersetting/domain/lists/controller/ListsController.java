package com.sparta.springusersetting.domain.lists.controller;

import com.sparta.springusersetting.domain.lists.service.ListsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListsController {
    private final ListsService listService;
}
