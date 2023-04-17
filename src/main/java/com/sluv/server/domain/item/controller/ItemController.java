package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.dto.PlaceRankResDto;
import com.sluv.server.domain.item.service.ItemService;
import com.sluv.server.domain.item.service.PlaceRankService;
import com.sluv.server.domain.item.service.TempItemService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
public class ItemController {

    private final ItemService itemService;
    private final PlaceRankService placeRankService;

    @Operation(
            summary = "아이템 등록",
            description = "아이템 등록 요청"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postItem(@AuthenticationPrincipal User user, @RequestBody ItemPostReqDto reqDto){

        itemService.postItem(user, reqDto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "인기 장소 조회",
            description = "인기 장소 조회. 상위 10개"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/place/top")
    public ResponseEntity<SuccessDataResponse<List<PlaceRankResDto>>> getTopPlace(){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<PlaceRankResDto>>builder()
                        .result(placeRankService.getTopPlace())
                        .build()
        );
    }
}
