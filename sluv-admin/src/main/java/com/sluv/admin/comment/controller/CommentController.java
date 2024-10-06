package com.sluv.admin.comment.controller;

import com.sluv.admin.comment.dto.CommentReportDetailResponse;
import com.sluv.admin.comment.dto.CommentReportInfoDto;
import com.sluv.admin.comment.dto.UpdateCommentReportResDto;
import com.sluv.admin.comment.service.CommentReportService;
import com.sluv.admin.common.response.ErrorResponse;
import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.response.SuccessDataResponse;
import com.sluv.domain.common.enums.ReportStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backoffice/comment")
public class CommentController {

    private final CommentReportService commentReportService;

    @Operation(
            summary = "댓글 신고 정보 조히",
            description = "WAITING, COMPLETED, REJECTED 로 검색 조건, 없으면 전체 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/report")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<CommentReportInfoDto>>> getAllCommentReport(Pageable pageable,
                                                                                                             @RequestParam(required = false) ReportStatus reportStatus) {
        PaginationResponse<CommentReportInfoDto> response = commentReportService.getAllCommentReport(pageable, reportStatus);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "댓글 신고 상세 정보 조히",
            description = "댓글 신고 id를 통해 댓글 신고 상세 정보 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/report/{commentReportId}")
    public ResponseEntity<SuccessDataResponse<CommentReportDetailResponse>> getCommentReportDetail(@PathVariable Long commentReportId) {
        CommentReportDetailResponse response = commentReportService.getCommentReportDetail(commentReportId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "댓글 신고 처리",
            description = "댓글 신고 id와 reportStatus(COMPLETED, REJECTED)를 통해 댓글 신고 처리"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/report/{commentReportId}")
    public ResponseEntity<SuccessDataResponse<UpdateCommentReportResDto>> changeCommentReportStatus(@PathVariable Long commentReportId,
                                                                                                    @RequestParam ReportStatus reportStatus) {
        UpdateCommentReportResDto response = commentReportService.updateCommentReportStatus(commentReportId, reportStatus);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
