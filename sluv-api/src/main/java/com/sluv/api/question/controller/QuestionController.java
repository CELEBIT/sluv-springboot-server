package com.sluv.api.question.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.question.dto.*;
import com.sluv.api.question.service.QuestionService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/question")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "*찾아주세요 게시글 등록",
            description = "User 토큰 필요. 생성: id -> null. 수정: id -> 해당 Question Id")
    @PostMapping("/find")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postFind(@CurrentUserId Long userId,
                                                                            @RequestBody QuestionFindPostReqDto dto) {
        QuestionPostResDto response = questionService.postQuestionFind(userId, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*이중에뭐살까 게시글 등록",
            description = "User 토큰 필요. 생성: id -> null. 수정: id -> 해당 Question Id")
    @PostMapping("/buy")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postBuy(@CurrentUserId Long userId,
                                                                           @RequestBody QuestionBuyPostReqDto dto) {

        QuestionPostResDto response = questionService.postQuestionBuy(userId, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*이거어때 게시글 등록",
            description = "User 토큰 필요. 생성: id -> null. 수정: id -> 해당 Question Id")
    @PostMapping("/how-about")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postHowabout(@CurrentUserId Long userId,
                                                                                @RequestBody QuestionHowaboutPostReqDto dto) {

        QuestionPostResDto response = questionService.postQuestionHowabout(userId, dto);

        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*추천해 줘 게시글 등록",
            description = "User 토큰 필요. 생성: id -> null. 수정: id -> 해당 Question Id")
    @PostMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<QuestionPostResDto>> postRecommend(@CurrentUserId Long userId,
                                                                                 @RequestBody QuestionRecommendPostReqDto dto) {
        QuestionPostResDto response = questionService.postQuestionRecommend(userId, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "Question 게시글 삭제",
            description = "데이터를 삭제 X -> QuestionStatus 변경 (ACTIVE -> DELETE)")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*Question 게시글 좋아요", description = "User 토큰 필요")
    @PostMapping("/{questionId}/like")
    public ResponseEntity<SuccessResponse> postQuestionLike(@CurrentUserId Long userId,
                                                            @PathVariable("questionId") Long questionId) {
        questionService.postQuestionLike(userId, questionId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*Question 게시글 신고",
            description = "User 토큰 필요. 중복 신고 시 [QuestionReportDuplicate] 예외 발생")
    @PostMapping("/{questionId}/report")
    public ResponseEntity<SuccessResponse> postQuestionReport(@CurrentUserId Long userId,
                                                              @PathVariable("questionId") Long questionId,
                                                              @RequestBody QuestionReportReqDto dto) {
        questionService.postQuestionReport(userId, questionId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*Question 게시글 상세조회", description = "User 토큰 필요")
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessDataResponse<QuestionGetDetailResDto>> postQuestionReport(
            @CurrentUserId Long userId, @PathVariable("questionId") Long questionId) {

        QuestionGetDetailResDto response = questionService.getQuestionDetail(userId, questionId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*QuestionBuy 게시글 투표", description = "User 토큰 필요. 좋아요 시스템과 동일.")
    @PostMapping("/{questionId}/vote")
    public ResponseEntity<SuccessResponse> postQuestionVote(@CurrentUserId Long userId,
                                                            @PathVariable("questionId") Long questionId,
                                                            @RequestBody QuestionVoteReqDto dto) {
        questionService.postQuestionVote(userId, questionId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*Question 기다리고 있어요",
            description = "qType에 따라 Question 타입 변경. questionId는 현재 Question은 제외하고 조회하기 위함.")
    @GetMapping("/wait")
    public ResponseEntity<SuccessDataResponse<List<QuestionSimpleResDto>>> getWaitQuestionBuy(
            @CurrentUserId Long userId,
            @RequestParam("questionId") Long questionId,
            @RequestParam("qType") String qType) {
        List<QuestionSimpleResDto> result = switch (qType) {
            case "Buy" -> questionService.getWaitQuestionBuy(userId, questionId);
            case "Find" -> questionService.getWaitQuestionFind(userId, questionId);
            case "How" -> questionService.getWaitQuestionHowabout(userId, questionId);
            case "Recommend" -> questionService.getWaitQuestionRecommend(userId, questionId);
            default -> throw new QuestionTypeNotFoundException();
        };

        return ResponseEntity.ok().body(SuccessDataResponse.create(result));
    }

    @Operation(summary = "Question 커뮤니티 게시글 종합 검색", description = "Pagination 적용. 최신순으로 조회")
    @GetMapping("/total")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> getQuestionTotalList(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<QuestionSimpleResDto> response = questionService.getTotalQuestionList(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "QuestionFind 커뮤니티 게시글 검색",
            description = "Pagination 적용. Ordering: 최신순으로 조회. Filtering: celebId.")
    @GetMapping("/find")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> getQuestionFindList(
            @CurrentUserId Long userId, @Nullable @RequestParam("celebId") Long celebId, Pageable pageable) {
        PaginationResponse<QuestionSimpleResDto> response = questionService.getQuestionFindList(userId, celebId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "QuestionBuy 커뮤니티 게시글 검색",
            description = """
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
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionBuySimpleResDto>>> getQuestionBuyList(
            @CurrentUserId Long userId, @Nullable @RequestParam("voteStatus") String voteStatus,
            Pageable pageable) {
        PaginationResponse<QuestionBuySimpleResDto> response = questionService.getQuestionBuyList(userId,
                voteStatus, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "QuestionHowabout 커뮤니티 게시글 검색", description = "Pagination 적용. Ordering 최신순")
    @GetMapping("/howabout")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> getQuestionHowaboutList(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<QuestionSimpleResDto> response = questionService.getQuestionHowaboutList(
                userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "QuestionRecommend 커뮤니티 게시글 검색",
            description = "Pagination 적용. Ordering 최신순. Filtering 전체, 특정해시태그")
    @GetMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> getQuestionRecommendList(
            @CurrentUserId Long userId, @Nullable @RequestParam String hashtag, Pageable pageable) {
        PaginationResponse<QuestionSimpleResDto> response = questionService.getQuestionRecommendList(
                userId, hashtag, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "일간 핫 커뮤니티 게시글 검색",
            description = " 10개 조회. Ordering 인기순(조회수 + 좋아요 수 + 댓글 수). 매일 00시 00분 00초를 기준으로 업데이트")
    @GetMapping("/dailyhot")
    public ResponseEntity<SuccessDataResponse<List<QuestionHomeResDto>>> getDailyHotQuestionList(@CurrentUserId Long userId) {
        List<QuestionHomeResDto> response = questionService.getDailyHotQuestionList(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "주간 핫 커뮤니티 게시글 검색",
            description = "Pagination 적용. Ordering 조회수 + 좋아요 수 + 댓글 수. Filtering 현재를 기점으로 일주일간 작성된 글")
    @GetMapping("/weeklyhot")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> getWeeklyHotQuestionList(
            Pageable pageable) {
        PaginationResponse<QuestionSimpleResDto> response = questionService.getWeeklyHotQuestionList(
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
