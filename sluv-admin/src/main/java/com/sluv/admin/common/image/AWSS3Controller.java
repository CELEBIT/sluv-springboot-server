package com.sluv.admin.common.image;

import com.sluv.admin.common.response.SuccessDataResponse;
import com.sluv.infra.s3.AWSS3Service;
import com.sluv.infra.s3.ImgExtension;
import com.sluv.infra.s3.PreSingedUrlResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/s3/pre-signed-url")
public class AWSS3Controller {
    private final AWSS3Service awss3Service;

    @PostMapping("/brand")
    public ResponseEntity<SuccessDataResponse<PreSingedUrlResDto>> getUserProfileUrl(
            @RequestParam("name") String name, @RequestParam("imgExtension") ImgExtension imgExtension) {
        PreSingedUrlResDto response = awss3Service.forBrandImage(name, imgExtension);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
