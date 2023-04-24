package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.brand.dto.RecentBrandReqDto;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.dto.RecentCelebResDto;
import com.sluv.server.domain.celeb.dto.RecentSearchCelebReqDto;
import com.sluv.server.domain.celeb.entity.RecentSearchCeleb;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.domain.celeb.service.RecentSearchCelebService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb")
public class CelebController {
    private final CelebService celebService;
    private final RecentSearchCelebService recentSearchCelebService;

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
            summary = "최근 검색 Celeb 조회",
            description = "유저가 최근 검색한 20개의 Celeb 조회",
            parameters = {@Parameter(name = "X-AUTH-TOKEN", required = true, description = "Authentication token", in = ParameterIn.HEADER)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search/recent")
    public ResponseEntity<SuccessDataResponse<List<RecentCelebResDto>>> searchUserRecentSearchCeleb(

            @AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<RecentCelebResDto>>builder()
                        .result(celebService.getUserRecentSearchCeleb(user))
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
            summary = "최근 선택한 셀럽 등록",
            description = "최근 선택 셀럽을 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/search/recent")
    public ResponseEntity<SuccessResponse> postRecentCeleb(@AuthenticationPrincipal User user, @RequestBody RecentSearchCelebReqDto dto ){

        recentSearchCelebService.postRecentSearchCeleb(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
