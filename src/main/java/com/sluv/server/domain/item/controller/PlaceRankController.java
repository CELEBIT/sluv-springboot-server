package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.HotPlaceResDto;
import com.sluv.server.domain.item.dto.PlaceRankReqDto;
import com.sluv.server.domain.item.dto.PlaceRankResDto;
import com.sluv.server.domain.item.service.ItemService;
import com.sluv.server.domain.item.service.PlaceRankService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/place")
public class PlaceRankController {

    private final PlaceRankService placeRankService;
    private final ItemService itemService;

    @Operation(summary = "인기 장소 조회", description = "인기 장소 조회. 상위 10개")
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<HotPlaceResDto>>> getTopPlace() {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<HotPlaceResDto>>builder()
                        .result(itemService.getTopPlace())
                        .build()
        );
    }

    @Operation(summary = "*유저가 최근 입력한 장소 검색", description = "User 토큰 필요")
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<List<PlaceRankResDto>>> getRecentPlaceTop20(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<PlaceRankResDto>>builder()
                        .result(placeRankService.getRecentPlaceTop20(user))
                        .build()
        );
    }

    @Operation(summary = "*최근 입력한 장소 등록", description = "User 토큰 필요.")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postPlace(@AuthenticationPrincipal User user,
                                                     @RequestBody PlaceRankReqDto dto) {
        placeRankService.postPlace(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저의 최근 입력한 장소 모두 삭제", description = "User 토큰 필요.")
    @DeleteMapping("/all")
    public ResponseEntity<SuccessResponse> deleteAllPlace(@AuthenticationPrincipal User user) {
        placeRankService.deleteAllPlace(user);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저의 특정 최근 입력한 장소 삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deletePlace(@AuthenticationPrincipal User user,
                                                       @RequestParam("placename") String placeName) {
        placeRankService.deletePlace(user, placeName);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
