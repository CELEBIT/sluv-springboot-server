package com.sluv.infra.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class AWSS3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String BUCKET_NAME;

    @Value("${aws.s3.brandBucketName}")
    private String BRAND_BUCKET_NAME;

    @Value("${aws.s3.baseUrl}")
    private String BASE_URL = "";

    /**
     * user profile item image 커뮤니티 이미지 클로젯 커버
     */
    public PreSingedUrlResDto forUserProfile(ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String fileName = getForUserProfileFileName(fixedExtension);
        log.info(fileName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName, fixedExtension));
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(fileName)
                .build();
    }

    public PreSingedUrlResDto forItem(ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String fileName = getForItemFileName(fixedExtension);
        log.info(fileName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName, fixedExtension));
        log.info("URL: {}", url.toString());
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(fileName)
                .build();
    }

    public PreSingedUrlResDto forCommunityPost(ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String fileName = getForCommunityPostFileName(fixedExtension);
        log.info(fileName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName, fixedExtension));
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(fileName)
                .build();
    }

    public PreSingedUrlResDto forCommunityComment(ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String fileName = getForCommunityCommentFileName(fixedExtension);
        log.info(fileName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName, fixedExtension));
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(fileName)
                .build();
    }

    public PreSingedUrlResDto forCloset(ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String fileName = getForClosetFileName(fixedExtension);
        log.info(fileName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName, fixedExtension));

        log.info("URL: {}", url.toString());
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(fileName)
                .build();
    }

    /**
     * 브랜드 이미지
     */
    public PreSingedUrlResDto forBrandImage(String fileName, ImgExtension imgExtension) {
        String fixedExtension = imgExtension.getUploadExtension();
        String filePathName = getForBrandImageFileName(fileName, fixedExtension);
        log.info(filePathName);
        URL url =
                amazonS3.generatePresignedUrl(
                        getGeneratePreSignedUrlRequest(BRAND_BUCKET_NAME, filePathName, fixedExtension));
        return PreSingedUrlResDto.builder()
                .preSignedUrl(url.toString())
                .key(filePathName)
                .build();
    }

    // 유저 프로필
    private String getForUserProfileFileName(String imgExtension) {
        return BASE_URL + "/user/profile/" + UUID.randomUUID() + "." + imgExtension;
    }

    // 아이템 이미지
    private String getForItemFileName(String imgExtension) {
        return BASE_URL + "/item/" + UUID.randomUUID() + "." + imgExtension;
    }

    // 커뮤니티 게시글 이미지
    private String getForCommunityPostFileName(String imgExtension) {
        return BASE_URL + "/community/post/" + UUID.randomUUID() + "." + imgExtension;
    }

    // 커뮤니티 댓글 이미지
    private String getForCommunityCommentFileName(String imgExtension) {
        return BASE_URL + "/community/comment/" + UUID.randomUUID() + "." + imgExtension;
    }

    //클로젯 커버
    private String getForClosetFileName(String imgExtension) {
        return BASE_URL + "/closet/" + UUID.randomUUID() + "." + imgExtension;
    }

    // 브랜드 이미지
    private String getForBrandImageFileName(String fileName, String imgExtension) {
        return "brand_img/" + fileName + "." + imgExtension;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName,
                                                                       String imgExtension) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withKey(fileName)
                        .withContentType("image/" + imgExtension)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 100000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }
}