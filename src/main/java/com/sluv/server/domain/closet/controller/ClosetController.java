package com.sluv.server.domain.closet.controller;

import com.sluv.server.domain.closet.dto.ClosetListCountResDto;
import com.sluv.server.domain.closet.dto.ClosetNameCheckResDto;
import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/closet")
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;

    @Operation(summary = "*현재 유저의 옷장 리스트 조회", description = "User 토큰 필요")
    @GetMapping("/list")
    public ResponseEntity<SuccessDataResponse<ClosetListCountResDto>> getClosetList(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ClosetListCountResDto>builder()
                        .result(closetService.getClosetList(user))
                        .build()
        );
    }

    @Operation(summary = "옷장 이름 중복 검사", description = "옷장 등록 및 수정 시 호출")
    @GetMapping("/check-name")
    public ResponseEntity<SuccessDataResponse<ClosetNameCheckResDto>> checkClosetNameDuplicated(
            @RequestParam("name") String name, @Nullable @RequestParam("id") Long closetId) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ClosetNameCheckResDto>builder()
                        .result(closetService.checkClosetNameDuplicated(name, closetId))
                        .build()
        );
    }

    @Operation(summary = "*옷장 생성", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postCloset(@AuthenticationPrincipal User user,
                                                      @RequestBody ClosetReqDto dto) {
        closetService.postCloset(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*옷장 커버정보 수정", description = "User 토큰 필요")
    @PutMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> patchCloset(@AuthenticationPrincipal User user,
                                                       @PathVariable("closetId") Long closetId,
                                                       @RequestBody ClosetReqDto dto) {
        closetService.patchCloset(user, closetId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    /**
     * 삭제 시 Closet 데이터를 DB에서 제거
     */
    @Operation(summary = "*옷장 삭제", description = "User 토큰 필요. 기본 Closet은 삭제 불가.")
    @DeleteMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> deleteCloset(@AuthenticationPrincipal User user,
                                                        @PathVariable("closetId") Long closetId) {
        closetService.deleteCloset(user, closetId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
