package com.sluv.api.item.controller;

import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.item.dto.TempItemCountResDto;
import com.sluv.api.item.dto.TempItemPostReqDto;
import com.sluv.api.item.dto.TempItemPostResDto;
import com.sluv.api.item.dto.TempItemResDto;
import com.sluv.api.item.service.TempItemService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/temp")
@Slf4j
public class TempItemController {
    private final TempItemService tempItemService;

    @Operation(summary = "*임시저장 아이템 리스트 조회", description = "User 토큰 필요")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<TempItemResDto>>> getTempItemList(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<TempItemResDto> response = tempItemService.getTempItemList(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*임시저장 아이템 갯수 조회", description = "User 토큰 필요")
    @GetMapping("/count")
    public ResponseEntity<SuccessDataResponse<TempItemCountResDto>> getTempItemCount(
            @CurrentUserId Long userId) {
        TempItemCountResDto response = tempItemService.countTempItemCount(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*Item 임시저장", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<TempItemPostResDto>> postTempItem(@CurrentUserId Long userId,
                                                                                @RequestBody TempItemPostReqDto reqDto) {
        TempItemPostResDto response = TempItemPostResDto.of(tempItemService.postTempItem(userId, reqDto));
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "임시저장 아이템 선택삭제", description = "User 토큰 필요")
    @DeleteMapping("/{tempItemId}")
    public ResponseEntity<SuccessResponse> deleteTempItem(@PathVariable Long tempItemId) {
        tempItemService.deleteTempItem(tempItemId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*임시저장 아이템 전체삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllTempItem(@CurrentUserId Long userId) {
        tempItemService.deleteAllTempItem(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
