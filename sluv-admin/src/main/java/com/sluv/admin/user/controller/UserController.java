package com.sluv.admin.user.controller;

import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.response.SuccessDataResponse;
import com.sluv.admin.common.response.SuccessResponse;
import com.sluv.admin.user.dto.UpdateUserReportResDto;
import com.sluv.admin.user.dto.UserAdminInfoDto;
import com.sluv.admin.user.dto.UserReportInfoDto;
import com.sluv.admin.user.service.UserReportService;
import com.sluv.admin.user.service.UserService;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backoffice/user")
public class UserController {

    private final UserService userService;
    private final UserReportService userReportService;

    @Operation(
            summary = "전체 유저 조회",
            description = "모든 유저의 상태와 신고 수를 포함한 정보를 조회한다"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserAdminInfoDto>>> getAllUserInfo(Pageable pageable) {
        PaginationResponse<UserAdminInfoDto> response = userService.getAllUserInfo(pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "유저 상태 변경",
            description = "ACTIVE, BLOCKED, DELETED, PENDING_PROFILE, PENDING_CELEB 중에서 유저의 상태를 변경한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{userId}/status/{status}")
    public ResponseEntity<SuccessResponse> updateUserStatus(@PathVariable Long userId, @PathVariable UserStatus status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(
            summary = "유저 신고 정보 조회",
            description = "WAITING, COMPLETED, REJECTED 로 검색 조건, 없으면 전체 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/report")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserReportInfoDto>>> getAllUserReport(Pageable pageable,
                                                                                                       @RequestParam(required = false) ReportStatus reportStatus) {
        PaginationResponse<UserReportInfoDto> response = userReportService.getAllUserReport(pageable, reportStatus);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "유저 신고 처리",
            description = "유저 신고 id와 reportStatus(COMPLETED, REJECTED)를 통해 유저 신고 처리"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/report/{userReportId}")
    public ResponseEntity<SuccessDataResponse<UpdateUserReportResDto>> updateUserReportStatus(@PathVariable Long userReportId,
                                                                                              @RequestParam ReportStatus reportStatus) {
        UpdateUserReportResDto response = userReportService.updateUserReportStatus(userReportId, reportStatus);
        return ResponseEntity.ok().body(SuccessDataResponse.create((response)));
    }
}
