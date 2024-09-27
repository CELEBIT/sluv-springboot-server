package com.sluv.api.item.controller;

import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.item.dto.ItemEditReqDto;
import com.sluv.api.item.service.ItemEditReqService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
public class ItemEditController {
    private final ItemEditReqService itemEditReqService;

    @Operation(summary = "*아이템 게시글 수정 요청", description = "User 토큰 필요")
    @PostMapping("/{itemId}/edit-req")
    public ResponseEntity<SuccessResponse> postItemEdit(@CurrentUserId Long userId,
                                                        @PathVariable(name = "itemId") Long itemId,
                                                        @RequestBody ItemEditReqDto dto) {
        itemEditReqService.postItemEdit(userId, itemId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
