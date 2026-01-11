package com.sluv.api.item.controller;

import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.item.dto.HotPlaceResDto;
import com.sluv.api.item.dto.PlaceRankReqDto;
import com.sluv.api.item.dto.PlaceRankResDto;
import com.sluv.api.item.service.ItemService;
import com.sluv.api.item.service.PlaceRankService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/place")
public class PlaceRankController {

    private final PlaceRankService placeRankService;
    private final ItemService itemService;

    @Operation(summary = "인기 장소 조회", description = "인기 장소 조회. 상위 10개")
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<HotPlaceResDto>>> getTopPlace() {
        List<HotPlaceResDto> response = itemService.getTopPlace();
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*유저가 최근 입력한 장소 검색", description = "User 토큰 필요")
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<List<PlaceRankResDto>>> getRecentPlaceTop20(
            @CurrentUserId Long userId) {
        List<PlaceRankResDto> response = placeRankService.getRecentPlaceTop20(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*최근 입력한 장소 등록", description = "User 토큰 필요.")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postPlace(@CurrentUserId Long userId,
                                                     @RequestBody PlaceRankReqDto dto) {
        placeRankService.postPlace(userId, dto);

        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 최근 입력한 장소 모두 삭제", description = "User 토큰 필요.")
    @DeleteMapping("/all")
    public ResponseEntity<SuccessResponse> deleteAllPlace(@CurrentUserId Long userId) {
        placeRankService.deleteAllPlace(userId);

        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 특정 최근 입력한 장소 삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deletePlace(@CurrentUserId Long userId,
                                                       @RequestParam("placename") String placeName) {
        placeRankService.deletePlace(userId, placeName);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
