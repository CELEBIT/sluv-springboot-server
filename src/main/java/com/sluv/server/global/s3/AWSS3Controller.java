package com.sluv.server.global.s3;

import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/s3/pre-signed-url")
public class AWSS3Controller {
    private final AWSS3Service awss3Service;

    @Operation(
            summary = "user 프로필 이미지 업로드",
            description = "user 프로필 이미지 업로드" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/user")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getUserProfileUrl(
            @RequestParam ImgExtension imgExtension) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forUserProfile(imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "아이템 이미지 업로드",
            description = "아이템 이미지 업로드" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/item")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getItemImgUrl(
            @RequestParam ImgExtension imgExtension) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forItem(imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "커뮤니티 게시글 이미지 업로드",
            description = "커뮤니티 게시글 이미지 업로드" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/question")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getQuestionImgUrl(
            @RequestParam ImgExtension imgExtension) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCommunityPost(imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "커뮤니티 댓글 이미지 업로드",
            description = "커뮤니티 댓글 이미지 업로드" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/comment")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getCommentImgUrl(
            @RequestParam ImgExtension imgExtension) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCommunityComment(imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "옷장 이미지 업로드",
            description = "옷장 이미지 업로드" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/closet")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getClosetImgUrl(
            @RequestParam ImgExtension imgExtension) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCloset(imgExtension)
                        )
                        .build()
        );
    }
}
