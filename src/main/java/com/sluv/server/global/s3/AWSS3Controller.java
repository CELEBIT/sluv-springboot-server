package com.sluv.server.global.s3;

import com.sluv.server.domain.user.entity.User;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/s3/pre-signed-url")
public class AWSS3Controller {
    private final AWSS3Service awss3Service;

    @Operation(
            summary = "user 프로필 이미지 업로드",
            description = "user 프로필 이미지 업로드" +
                    "\n (User Id Token 필요)" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/user")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getUserProfileUrl(@AuthenticationPrincipal User user, @RequestParam ImgExtension imgExtension){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forUserProfile(user, imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "아이템 이미지 업로드",
            description = "아이템 이미지 업로드" +
                    "\n Item Id 필요" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/item/{itemId}")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getItemImgUrl(@PathVariable("itemId") Long itemId, @RequestParam ImgExtension imgExtension){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forItem(itemId, imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "커뮤니티 게시글 이미지 업로드",
            description = "커뮤니티 게시글 이미지 업로드" +
                    "\n Question Id 필요" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/question/{questionId}")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getQuestionImgUrl(@PathVariable("questionId") Long questionId, @RequestParam ImgExtension imgExtension){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCommunityPost(questionId, imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "커뮤니티 댓글 이미지 업로드",
            description = "커뮤니티 댓글 이미지 업로드" +
                    "\n Comment Id 필요" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getCommentImgUrl(@PathVariable("commentId") Long commentId, @RequestParam ImgExtension imgExtension){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCommunityComment(commentId, imgExtension)
                        )
                        .build()
        );
    }

    @Operation(
            summary = "옷장 이미지 업로드",
            description = "옷장 이미지 업로드" +
                    "\n Closet Id 필요" +
                    "\n imgExtension: 이미지 확장자"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/closet/{closetId}")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getClosetImgUrl(@PathVariable("closetId") Long closetId, @RequestParam ImgExtension imgExtension){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PreSingedUrlResDto>builder()
                        .result(
                                awss3Service.forCloset(closetId, imgExtension)
                        )
                        .build()
        );
    }
}
