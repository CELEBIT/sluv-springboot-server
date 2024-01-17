package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemReportReqDto;
import com.sluv.server.domain.item.service.ItemReportService;
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
public class ItemReportController {
    private final ItemReportService itemReportService;

    @Operation(summary = "*아이템 게시글 신고", description = "User 토큰 필요")
    @PostMapping("/{itemId}/report")
    public ResponseEntity<SuccessResponse> postItemReport(@AuthenticationPrincipal User user,
                                                          @PathVariable(name = "itemId") Long itemId,
                                                          @RequestBody ItemReportReqDto dto) {
        itemReportService.postItemReport(user, itemId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
