package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.service.NewBrandService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/newbrand")
public class NewBrandController {

    private final NewBrandService newBrandService;

    @Operation(
            summary = "NewBrand 생성",
            description = "새로운 Brand 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<NewBrandPostResDto>> postNewBrand(@RequestBody NewBrandPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<NewBrandPostResDto>builder()
                        .result(newBrandService.postNewBrand(dto))
                        .build()
                );
    }
}
