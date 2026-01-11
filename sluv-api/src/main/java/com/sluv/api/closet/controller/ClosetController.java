package com.sluv.api.closet.controller;

import com.sluv.api.closet.dto.request.ClosetRequest;
import com.sluv.api.closet.dto.response.ClosetListCountResponse;
import com.sluv.api.closet.dto.response.ClosetNameCheckResponse;
import com.sluv.api.closet.service.ClosetService;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/closet")
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;

    @Operation(summary = "*현재 유저의 옷장 리스트 조회", description = "User 토큰 필요")
    @GetMapping("/list")
    public ResponseEntity<SuccessDataResponse<ClosetListCountResponse>> getClosetList(@CurrentUserId Long userId) {
        ClosetListCountResponse response = closetService.getClosetList(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "옷장 이름 중복 검사", description = "옷장 등록 및 수정 시 호출")
    @GetMapping("/check-name")
    public ResponseEntity<SuccessDataResponse<ClosetNameCheckResponse>> checkClosetNameDuplicated(
            @RequestParam("name") String name, @Nullable @RequestParam("id") Long closetId) {
        ClosetNameCheckResponse response = closetService.checkClosetNameDuplicated(name, closetId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*옷장 생성", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postCloset(@CurrentUserId Long userId, @RequestBody ClosetRequest request) {
        closetService.postCloset(userId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*옷장 커버정보 수정", description = "User 토큰 필요")
    @PutMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> patchCloset(@CurrentUserId Long userId,
                                                       @PathVariable("closetId") Long closetId,
                                                       @RequestBody ClosetRequest request) {
        closetService.patchCloset(userId, closetId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    /**
     * 삭제 시 Closet 데이터를 DB에서 제거
     */
    @Operation(summary = "*옷장 삭제", description = "User 토큰 필요. 기본 Closet은 삭제 불가.")
    @DeleteMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> deleteCloset(@CurrentUserId Long userId,
                                                        @PathVariable("closetId") Long closetId) {
        closetService.deleteCloset(userId, closetId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
