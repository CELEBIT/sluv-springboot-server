package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.domain.user.entity.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/search/recent")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResDto>>> searchUserRecentSearchCeleb(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchResDto>>builder()
                        .result(celebService.getUserRecentSearchCeleb(user))
                        .build()
        );
    }
}
