package com.sluv.server.domain.question.controller;

import com.sluv.server.domain.question.dto.QuestionFindPostReqDto;
import com.sluv.server.domain.question.dto.QuestionFindPostResDto;
import com.sluv.server.domain.question.service.QuestionService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/question")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(
            summary = "*찾아주세요 게시글 등록",
            description = "찾아주세요 게시글을 등록하는 기능" +
                    "\n - User Id Token 필요"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/find")
    public ResponseEntity<SuccessDataResponse<QuestionFindPostResDto>> postFind(@AuthenticationPrincipal User user, @RequestBody QuestionFindPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionFindPostResDto>builder()
                        .result(questionService.postQuestionFind(user, dto))
                        .build()
        );
    }
}
