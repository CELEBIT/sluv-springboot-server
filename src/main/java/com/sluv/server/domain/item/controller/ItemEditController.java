package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemEditReqDto;
import com.sluv.server.domain.item.service.ItemEditReqService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
@Slf4j
public class ItemEditController {
    private final ItemEditReqService itemEditReqService;

    @Operation(summary = "*아이템 게시글 수정 요청", description = "User 토큰 필요")
    @PostMapping("/{itemId}/edit-req")
    public ResponseEntity<SuccessResponse> postItemEdit(@AuthenticationPrincipal User user,
                                                        @PathVariable(name = "itemId") Long itemId,
                                                        @RequestBody ItemEditReqDto dto) {
        itemEditReqService.postItemEdit(user, itemId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
