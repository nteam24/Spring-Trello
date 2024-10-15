package com.sparta.springusersetting.domain.lists.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.lists.dto.request.ListsRequestDto;
import com.sparta.springusersetting.domain.lists.dto.response.GetListsResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsCreateResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsResponseDto;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.service.ListsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}")
@RequiredArgsConstructor
public class ListsController {
    private final ListsService listsService;

    /**
     * 리스트 생성
     * @param boardId
     * @param requestDto
     * @param authUser
     * @return 생성된 리스트 정보
     */
    @PostMapping("/lists")
    public ResponseEntity<ApiResponse<ListsCreateResponseDto>> createLists(@PathVariable Long boardId,
                                                                          @RequestBody ListsRequestDto requestDto,
                                                                          @AuthenticationPrincipal AuthUser authUser){
        ListsCreateResponseDto response = listsService.createLists(boardId, requestDto, authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리스트 수정
     * @param boardId
     * @param listsId
     * @param requestDto
     * @param authUser
     * @return 수정된 리스트 정보
     */
    @PatchMapping("/lists/{listsId}")
    public ResponseEntity<ApiResponse<ListsResponseDto>> updateLists(@PathVariable Long boardId,
                                                                    @PathVariable Long listsId,
                                                                    @RequestBody ListsRequestDto requestDto,
                                                                    @AuthenticationPrincipal AuthUser authUser){
        ListsResponseDto response = listsService.updateLists(boardId, listsId, requestDto, authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리스트 위치 수정
     * @param boardId
     * @param listsId
     * @param pos
     * @param authUser
     * @return 수정된 리스트 정보
     */
    @PatchMapping("/lists/{listsId}/pos")
    public ResponseEntity<ApiResponse<ListsResponseDto>> updateListsPosition(@PathVariable Long boardId,
                                                                             @PathVariable Long listsId,
                                                                             @RequestParam Integer pos,
                                                                             @AuthenticationPrincipal AuthUser authUser){
        ListsResponseDto response = listsService.updateListsPosition(boardId, listsId, pos, authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리스트 단건 조회
     * @param boardId
     * @param listsId
     * @param authUser
     * @return 리스트 정보
     */
    @GetMapping("/lists/{listsId}")
    public ResponseEntity<ApiResponse<GetListsResponseDto>> getLists(@PathVariable Long boardId,
                                                                     @PathVariable Long listsId,
                                                                     @AuthenticationPrincipal AuthUser authUser){
        GetListsResponseDto response = listsService.getLists(boardId, listsId, authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리스트 삭제
     * @param boardId
     * @param listsId
     * @param authUser
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/lists/{listsId}")
    public ResponseEntity<ApiResponse<String>> deleteLists(@PathVariable Long boardId,
                                                          @PathVariable Long listsId,
                                                          @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(listsService.deleteLists(boardId, listsId, authUser.getUserId())));
    }
}
