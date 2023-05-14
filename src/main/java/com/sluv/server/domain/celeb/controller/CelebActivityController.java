package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.CelebActivityResDto;
import com.sluv.server.domain.celeb.service.CelebActivityService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb/activity")
public class CelebActivityController {
    private final CelebActivityService celebActivityService;


    @Operation(
            summary = "셀럽의 활동 목록 조회",
            description = "중복 이름을 가진 셀럽을 분류할 때 사용할 셀럽의 활동 목록을 조회하는 API" +
                    "\n - [최신순으로 8개를 조회]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{celebId}")
    public ResponseEntity<SuccessDataResponse<List<CelebActivityResDto>>> getCelebActivity(@PathVariable("celebId") Long celebId){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebActivityResDto>>builder()
                        .result(celebActivityService.getCelebActivity(celebId))
                        .build()
        );

    }
}
