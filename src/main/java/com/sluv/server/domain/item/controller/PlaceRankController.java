package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.PlaceRankReqDto;
import com.sluv.server.domain.item.dto.HotPlaceResDto;
import com.sluv.server.domain.item.dto.PlaceRankResDto;
import com.sluv.server.domain.item.service.ItemService;
import com.sluv.server.domain.item.service.PlaceRankService;
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
@RequestMapping("/app/item/place")
public class PlaceRankController {

    private final PlaceRankService placeRankService;
    private final ItemService itemService;

    @Operation(
            summary = "인기 장소 조회",
            description = "인기 장소 조회. 상위 10개"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<HotPlaceResDto>>> getTopPlace(){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<HotPlaceResDto>>builder()
                        .result(itemService.getTopPlace())
                        .build()
        );
    }

    @Operation(
            summary = "*최근 입력한 장소 등록",
            description = "최근 입력한 장소를 PlaceRank 테이블에 등록 " +
                    "User Id 가 필요함."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postPlace(@AuthenticationPrincipal User user, @RequestBody PlaceRankReqDto dto){
        placeRankService.postPlace(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*유저의 최근 입력한 장소 모두 삭제",
            description = "PlaceRank 테이블에서 유저의 최근 입력한 장소 모두 삭제 " +
                    "User Id 가 필요함."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/all")
    public ResponseEntity<SuccessResponse> deleteAllPlace(@AuthenticationPrincipal User user){
        placeRankService.deleteAllPlace(user);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*유저의 특정 최근 입력한 장소 삭제",
            description = "PlaceRank 테이블에서 유저의 특정 최근 입력한 장소 삭제 " +
                    "User Id 가 필요함."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deletePlace(@AuthenticationPrincipal User user, @RequestParam("placename") String placeName){
        placeRankService.deletePlace(user, placeName);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*유저가 최근 입력한 장소 검색",
            description = "유저가 최근 입력한 장소 최근 20개 검색" +
                    "User Id 가 필요함."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<List<PlaceRankResDto>>> getRecentPlaceTop20(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<PlaceRankResDto>>builder()
                                    .result(placeRankService.getRecentPlaceTop20(user))
                                    .build()
                );
    }
}
