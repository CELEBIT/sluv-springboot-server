package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.service.ItemService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> postItem(@AuthenticationPrincipal User user, @RequestBody ItemPostReqDto reqDto){

        itemService.postItem(user, reqDto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
