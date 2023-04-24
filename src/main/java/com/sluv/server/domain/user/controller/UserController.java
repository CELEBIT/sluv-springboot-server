package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/user")
public class UserController {
    private final UserService userService;
    @Operation(
            summary = "유저의 관심 샐럽 조회",
            description = "유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResDto>>> getInterestedCeleb(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebParentResDto>>builder()
                                                            .result(userService.getInterestedCeleb(user))
                                                            .build());
    }


}
