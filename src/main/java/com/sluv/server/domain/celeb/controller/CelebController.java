package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.CelebSearchByCategoryResDto;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb")
public class CelebController {
    private final CelebService celebService;

    @Operation(
            summary = "Celeb 검색",
            description = "입력한 이름으로 Celeb을 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResDto>>> searchCelebByName(@RequestParam String celebName, Pageable pageable){


        return ResponseEntity.ok().body(
            SuccessDataResponse.<List<CelebSearchResDto>>builder()
                    .result(celebService.searchCeleb(celebName, pageable))
                    .build()
        );
    }

    @Operation(
            summary = "인기 셀럽 조회",
            description = "조회가 많이된 Celeb 상위 10개 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResDto>>> searchTop10Celeb(){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchResDto>>builder()
                        .result(celebService.getTop10Celeb())
                        .build()
        );
    }
    @Operation(
            summary = "관심 셀럽 조회 시, 카테고리별 셀럽 조회",
            description = "카테고리별 최대 30개를 한번에 전달, 카테고리는 순서 X, 셀럽는 가나다 순서."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/each/category")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchByCategoryResDto>>> searchCelebByCategory(){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchByCategoryResDto>>builder()
                        .result(celebService.getCelebByCategory())
                        .build()
        );
    }
}
