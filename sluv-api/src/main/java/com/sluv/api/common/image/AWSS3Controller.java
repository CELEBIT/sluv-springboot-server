package com.sluv.api.common.image;

import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.infra.s3.AWSS3Service;
import com.sluv.infra.s3.ImgExtension;
import com.sluv.infra.s3.PreSingedUrlResDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "user 프로필 이미지 업로드", description = "user 프로필 이미지 업로드\n imgExtension: 이미지 확장자")
    @PostMapping("/user")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getUserProfileUrl(
            @RequestParam ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forUserProfile(imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "아이템 이미지 업로드", description = "아이템 이미지 업로드\n imgExtension: 이미지 확장자")
    @PostMapping("/item")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getItemImgUrl(
            @RequestParam ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forItem(imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "커뮤니티 게시글 이미지 업로드", description = "커뮤니티 게시글 이미지 업로드\n imgExtension: 이미지 확장자")
    @PostMapping("/question")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getQuestionImgUrl(
            @RequestParam ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forCommunityPost(imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "커뮤니티 댓글 이미지 업로드", description = "커뮤니티 댓글 이미지 업로드\n imgExtension: 이미지 확장자")
    @PostMapping("/comment")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getCommentImgUrl(
            @RequestParam ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forCommunityComment(imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "옷장 이미지 업로드", description = "옷장 이미지 업로드\n imgExtension: 이미지 확장자")
    @PostMapping("/closet")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getClosetImgUrl(
            @RequestParam ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forCloset(imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }
}
