package com.sluv.api.item.controller;

import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.item.dto.ItemReportReqDto;
import com.sluv.api.item.service.ItemReportService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
public class ItemReportController {
    private final ItemReportService itemReportService;

    @Operation(summary = "*아이템 게시글 신고", description = "User 토큰 필요")
    @PostMapping("/{itemId}/report")
    public ResponseEntity<SuccessResponse> postItemReport(@CurrentUserId Long userId,
                                                          @PathVariable(name = "itemId") Long itemId,
                                                          @RequestBody ItemReportReqDto dto) {
        itemReportService.postItemReport(userId, itemId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
