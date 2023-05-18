package com.sluv.server.domain.question.controller;

import com.sluv.server.domain.question.dto.*;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/question")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(
            summary = "*찾아주세요 게시글 등록",
            description = "찾아주세요 게시글을 등록하는 기능" +
                    "\n - User Id Token 필요" +
                    "\n 생성: id -> null" +
                    "\n 수정: id -> 해당 Question Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/find")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postFind(@AuthenticationPrincipal User user, @RequestBody QuestionFindPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionPostResDto>builder()
                        .result(questionService.postQuestionFind(user, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*이중에뭐살까 게시글 등록",
            description = "이중에뭐살까 게시글을 등록하는 기능" +
                    "\n - User Id Token 필요" +
                    "\n 생성: id -> null" +
                    "\n 수정: id -> 해당 Question Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/buy")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postBuy(@AuthenticationPrincipal User user, @RequestBody QuestionBuyPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionPostResDto>builder()
                        .result(questionService.postQuestionBuy(user, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*이거어때 게시글 등록",
            description = "이거어때 게시글을 등록하는 기능" +
                    "\n - User Id Token 필요" +
                    "\n 생성: id -> null" +
                    "\n 수정: id -> 해당 Question Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/how-about")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postHowabout(@AuthenticationPrincipal User user, @RequestBody QuestionHowaboutPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionPostResDto>builder()
                        .result(questionService.postQuestionHowabout(user, dto))
                        .build()
        );
    }
    @Operation(
            summary = "*추천해 줘 게시글 등록",
            description = "추천해 줘 게시글을 등록하는 기능" +
                    "\n - User Id Token 필요" +
                    "\n 생성: id -> null" +
                    "\n 수정: id -> 해당 Question Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postRecommend(@AuthenticationPrincipal User user, @RequestBody QuestionRecommendPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionPostResDto>builder()
                        .result(questionService.postQuestionRecommend(user, dto))
                        .build()
        );
    }

    @Operation(
            summary = "Question 게시글 삭제",
            description = "Question 게시글 삭제" +
                    "\n 관련된 데이터를 삭제하는 것이 아닌 Question의 상태만 변경" +
                    "\n ACTIVE -> DELETE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> deleteQuestion(@PathVariable("questionId") Long questionId){
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().body(
               new SuccessResponse()
        );
    }

    @Operation(
            summary = "*Question 게시글 좋아요",
            description = "Question 게시글 좋아요 기능" +
                    "\n User Id Token 필요"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{questionId}/like")
    public ResponseEntity<SuccessResponse> postQuestionLike(@AuthenticationPrincipal User user, @PathVariable("questionId") Long questionId){
        questionService.postQuestionLike(user, questionId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*Question 게시글 신고",
            description = "Question 게시글 신고 기능" +
                    "\n User Id Token 필요" +
                    "\n 중복 신고 시 [QuestionReportDuplicate] 예외 발생"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{questionId}/report")
    public ResponseEntity<SuccessResponse> postQuestionReport(@AuthenticationPrincipal User user, @PathVariable("questionId") Long questionId, @RequestBody QuestionReportReqDto dto){
        questionService.postQuestionReport(user, questionId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
