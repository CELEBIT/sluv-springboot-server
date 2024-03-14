package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.TempItemCountResDto;
import com.sluv.server.domain.item.dto.TempItemPostReqDto;
import com.sluv.server.domain.item.dto.TempItemPostResDto;
import com.sluv.server.domain.item.dto.TempItemResDto;
import com.sluv.server.domain.item.service.TempItemService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationCountResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<TempItemResDto>>> getTempItemList(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<TempItemResDto>>builder()
                        .result(tempItemService.getTempItemList(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*임시저장 아이템 갯수 조회", description = "User 토큰 필요")
    @GetMapping("/count")
    public ResponseEntity<SuccessDataResponse<TempItemCountResDto>> getTempItemCount(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<TempItemCountResDto>builder()
                        .result(tempItemService.countTempItemCount(user))
                        .build()
        );
    }

    @Operation(summary = "*Item 임시저장", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<TempItemPostResDto>> postTempItem(@AuthenticationPrincipal User user,
                                                                                @RequestBody TempItemPostReqDto reqDto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<TempItemPostResDto>builder()
                        .result(
                                TempItemPostResDto.of(
                                        tempItemService.postTempItem(user, reqDto)
                                )
                        )
                        .build()
        );
    }

    @Operation(summary = "임시저장 아이템 선택삭제", description = "User 토큰 필요")
    @DeleteMapping("/{tempItemId}")
    public ResponseEntity<SuccessResponse> deleteTempItem(@PathVariable Long tempItemId) {
        tempItemService.deleteTempItem(tempItemId);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @Operation(summary = "*임시저장 아이템 전체삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllTempItem(@AuthenticationPrincipal User user) {
        tempItemService.deleteAllTempItem(user);
        return ResponseEntity.ok().body(new SuccessResponse());
    }
}
