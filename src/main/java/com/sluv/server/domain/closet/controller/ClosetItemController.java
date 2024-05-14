package com.sluv.server.domain.closet.controller;

import com.sluv.server.domain.closet.dto.ClosetDetailResDto;
import com.sluv.server.domain.closet.dto.ClosetItemSelectReqDto;
import com.sluv.server.domain.closet.service.ClosetItemService;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/closet")
@RequiredArgsConstructor
public class ClosetItemController {
    private final ClosetItemService closetItemService;

    @Operation(summary = "*옷장 상세조회", description = "User 토큰 필요. Pagination 적용. 가장 최근 Scrap 한 순서대로 정렬.")
    @GetMapping("/{closetId}")
    public ResponseEntity<ClosetDetailResDto<ItemSimpleResDto>> getClosetDetails(@AuthenticationPrincipal User user,
                                                                                 @PathVariable("closetId") Long closetId,
                                                                                 Pageable pageable) {

        return ResponseEntity.ok().body(
                closetItemService.getClosetDetails(user, closetId, pageable)
        );
    }

    /**
     * 일단 1개의 아이템은 유저당 1개의 옷장에만 저장 가능 A유저가 가지고 있는 1번 2번 옷장에 저장 불가. 1번 혹은 2번에만 저장 가능 기획에 따라 변경 가능 - 24.01.10
     */
    @Operation(summary = "*옷장에 아이템 스크랩(저장하기)", description = "User 토큰 필요. 유저당 아이템 1개만 저장 가능)")
    @PostMapping("/{itemId}/scrap/{closetId}")
    public ResponseEntity<SuccessResponse> postItemScrapToCloset(@AuthenticationPrincipal User user,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("closetId") Long closetId) {
        closetItemService.postItemScrapToCloset(user, itemId, closetId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*옷장에 편집하기로 선택한 Item들을 다른 옷장으로 이동", description = "User 토큰 필요")
    @PatchMapping("/{fromClosetId}/{toClosetId}/items")
    public ResponseEntity<SuccessResponse> patchSaveCloset(@AuthenticationPrincipal User user,
                                                           @PathVariable("fromClosetId") Long fromClosetId,
                                                           @PathVariable("toClosetId") Long toClosetId,
                                                           @RequestBody ClosetItemSelectReqDto dto) {
        closetItemService.moveItemInCloset(user, fromClosetId, toClosetId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*옷장에 편집하기로 선택한 Item을 모두 삭제", description = "User 토큰 필요")
    @PatchMapping("/{closetId}/items")
    public ResponseEntity<SuccessResponse> patchItems(@AuthenticationPrincipal User user,
                                                      @PathVariable("closetId") Long closetId,
                                                      @RequestBody ClosetItemSelectReqDto dto) {
        closetItemService.removeSelectItemInCloset(user, closetId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*아이템 게시글에서 북마크 버튼으로 삭제 시", description = "User 토큰 필요")
    @DeleteMapping("/{itemId}/scrap")
    public ResponseEntity<SuccessResponse> deleteItemScrapFromCloset(@AuthenticationPrincipal User user,
                                                                     @PathVariable("itemId") Long itemId) {
        closetItemService.deleteItemScrapFromCloset(user, itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
