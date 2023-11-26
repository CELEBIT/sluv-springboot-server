package com.sluv.server.domain.question.controller;

import com.sluv.server.domain.question.dto.QuestionBuyPostReqDto;
import com.sluv.server.domain.question.dto.QuestionFindPostReqDto;
import com.sluv.server.domain.question.dto.QuestionGetDetailResDto;
import com.sluv.server.domain.question.dto.QuestionHomeResDto;
import com.sluv.server.domain.question.dto.QuestionHowaboutPostReqDto;
import com.sluv.server.domain.question.dto.QuestionPostResDto;
import com.sluv.server.domain.question.dto.QuestionRecommendPostReqDto;
import com.sluv.server.domain.question.dto.QuestionReportReqDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionVoteReqDto;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.service.QuestionService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postFind(@AuthenticationPrincipal User user,
                                                                            @RequestBody QuestionFindPostReqDto dto) {
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
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postBuy(@AuthenticationPrincipal User user,
                                                                           @RequestBody QuestionBuyPostReqDto dto) {

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
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postHowabout(@AuthenticationPrincipal User user,
                                                                                @RequestBody QuestionHowaboutPostReqDto dto) {

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
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postRecommend(@AuthenticationPrincipal User user,
                                                                                 @RequestBody QuestionRecommendPostReqDto dto) {

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
    public ResponseEntity<SuccessResponse> deleteQuestion(@PathVariable("questionId") Long questionId) {
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
    public ResponseEntity<SuccessResponse> postQuestionLike(@AuthenticationPrincipal User user,
                                                            @PathVariable("questionId") Long questionId) {
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
    public ResponseEntity<SuccessResponse> postQuestionReport(@AuthenticationPrincipal User user,
                                                              @PathVariable("questionId") Long questionId,
                                                              @RequestBody QuestionReportReqDto dto) {
        questionService.postQuestionReport(user, questionId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*Question 게시글 상세조회",
            description = "Question 게시글을 상세조회하는 기능" +
                    "\n User Id Token 필요 -> 현재 유저가 작성한 게시글인지 판단하기 위함"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessDataResponse<QuestionGetDetailResDto>> postQuestionReport(
            @AuthenticationPrincipal User user, @PathVariable("questionId") Long questionId) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<QuestionGetDetailResDto>builder()
                        .result(
                                questionService.getQuestionDetail(user, questionId)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "*QuestionBuy 게시글 투표",
            description = """
                    QuestionBuy 게시글 투표 \n
                    최초 호출 -> QuestionVote 등록 \n
                    재호출 -> QuestionVote 삭제 \n
                    좋아요 시스템과 동일.
                    """
    )
    @PostMapping("/{questionId}/vote")
    public ResponseEntity<SuccessResponse> postQuestionVote(@AuthenticationPrincipal User user,
                                                            @PathVariable("questionId") Long questionId,
                                                            @RequestBody QuestionVoteReqDto dto) {

        questionService.postQuestionVote(user, questionId, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*Question 기다리고 있어요",
            description = """
                    유저를 기다리고 있는 Question 조회\n
                    qType에 따라 Question 타입 변경\n
                    questionId는 현재 Question은 제외하고 조회하기 위함.
                    """
    )
    @GetMapping("/wait")
    public ResponseEntity<SuccessDataResponse<List<QuestionSimpleResDto>>> getWaitQuestionBuy(
            @AuthenticationPrincipal User user,
            @RequestParam("questionId") Long questionId,
            @RequestParam("qType") String qType) {
        List<QuestionSimpleResDto> result = switch (qType) {
            case "Buy" -> questionService.getWaitQuestionBuy(user, questionId);
            case "Find" -> questionService.getWaitQuestionFind(user, questionId);
            case "How" -> questionService.getWaitQuestionHowabout(user, questionId);
            case "Recommend" -> questionService.getWaitQuestionRecommend(user, questionId);
            default -> throw new QuestionTypeNotFoundException();
        };

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<QuestionSimpleResDto>>builder()
                        .result(result)
                        .build()
        );
    }

    @Deprecated
    @Operation(
            summary = "Question 커뮤니티 리스트 조회",
            description = """
                    Question 커뮤니티 리스트 조회\n
                    Pagination 적용
                    """
    )
    @GetMapping("/list")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionList(
            @Nullable @RequestParam("qType") String qType, Pageable pageable) {
        PaginationResDto<QuestionSimpleResDto> result = switch (qType) {
            case "Total" -> questionService.getTotalQuestionList(pageable);
            case "Buy" -> questionService.getQuestionBuyList(null, pageable);
            case "Find" -> questionService.getQuestionFindList(null, pageable);
            case "How" -> questionService.getQuestionHowaboutList(pageable);
            case "Recommend" -> questionService.getQuestionRecommendList(null, pageable);
            default -> throw new QuestionTypeNotFoundException();
        };

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(result)
                        .build()
        );
    }

    @Operation(
            summary = "Question 커뮤니티 게시글 종합 검색",
            description = """
                    Question 커뮤니티 게시글 종합 검색\n
                    - Pagination 적용
                    - 최신순으로 조회
                    """
    )
    @GetMapping("/total")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionTotalList(
            Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getTotalQuestionList(pageable))
                        .build()
        );
    }

    @Operation(
            summary = "QuestionFind 커뮤니티 게시글 검색",
            description = """
                    QuestionFind 커뮤니티 게시글 검색\n
                    - Pagination 적용
                    - Ordering: 최신순으로 조회
                    - Filtering: celebId
                    """
    )
    @GetMapping("/find")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionFindList(
            @Nullable @RequestParam("celebId") Long celebId, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getQuestionFindList(celebId, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "QuestionBuy 커뮤니티 게시글 검색",
            description = """
                    QuestionBuy 커뮤니티 게시글 검색\n
                    - Pagination 적용
                    - Filtering: 전체, 진행중, 마감임박, 종료
                    =Ordering=\n
                         * 전체 → 최신순\n
                         * 진행 중 → 최신순\n
                         * 종료 임박 → 종료 임박 순\n
                         * 종료 → 최신순\n
                    """
    )
    @GetMapping("/buy")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionBuyList(
            @Nullable @RequestParam("voteStatus") String voteStatus, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getQuestionBuyList(voteStatus, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "QuestionHowabout 커뮤니티 게시글 검색",
            description = """
                    QuestionHowabout 커뮤니티 게시글 검색\n
                    - Pagination 적용 \n
                    - Ordering 최신순
                    """
    )
    @GetMapping("/howabout")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionHowaboutList(
            Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getQuestionHowaboutList(pageable))
                        .build()
        );
    }

    @Operation(
            summary = "QuestionRecommend 커뮤니티 게시글 검색",
            description = """
                    QuestionRecommend 커뮤니티 게시글 검색\n
                    - Pagination 적용 \n
                    - Ordering 최신순 \n
                    - Filtering 전체, 특정해시태그
                    """
    )
    @GetMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getQuestionRecommendList(
            @Nullable @RequestParam String hashtag, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getQuestionRecommendList(hashtag, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "일간 핫 커뮤니티 게시글 검색",
            description = """
                    일간 핫 커뮤니티 게시글 검색\n
                    - 10개 \n
                    - Ordering 인기순\n
                        - 조회수 + 좋아요 수 + 댓글 수\n
                    - 매일 00시 00분 00초를 기준으로 업데이트
                    """
    )
    @GetMapping("/dailyhot")
    public ResponseEntity<SuccessDataResponse<List<QuestionHomeResDto>>> getDailyHotQuestionList() {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<QuestionHomeResDto>>builder()
                        .result(questionService.getDailyHotQuestionList())
                        .build()
        );
    }

    @Operation(
            summary = "주간 핫 커뮤니티 게시글 검색",
            description = """
                    일간 핫 커뮤니티 게시글 검색\n
                    - Pagination 적용 \n
                    - Ordering 조회수 + 좋아요 수 + 댓글 수 \n
                    - Filtering 현재를 기점으로 일주일간 작성된 글
                    """
    )
    @GetMapping("/weeklyhot")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> getWeeklyHotQuestionList(
            Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(questionService.getWeeklyHotQuestionList(pageable))
                        .build()
        );
    }
}
