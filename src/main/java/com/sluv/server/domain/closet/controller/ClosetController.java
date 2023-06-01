package com.sluv.server.domain.closet.controller;

import com.sluv.server.domain.closet.dto.ClosetItemDeleteReqDto;
import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/closet")
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;

    @Operation(
            summary = "*옷장 생성",
            description = "사용자의 옷장을 생성" +
                    "\n User Id Token 필요"
    )
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postCloset(@AuthenticationPrincipal User user, @RequestBody ClosetReqDto dto){
        closetService.postCloset(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*옷장 커버정보 수정",
            description = "사용자 옷장의 커버정보를 수정" +
                    "\n User Id Token 필요 -> 소유자인지 확인"
    )
    @PutMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> patchCloset(@AuthenticationPrincipal User user, @PathVariable("closetId") Long closetId , @RequestBody ClosetReqDto dto){
        closetService.patchCloset(user, closetId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*옷장 삭제",
            description = "사용자 옷장을 삭제" +
                    "\n User Id Token 필요 -> 소유자인지 확인" +
                    "\n 기본 Closet이라면 삭제 불가." +
                    "\n 삭제 시 Closet 데이터를 을 DB에서 제거"
    )
    @DeleteMapping("/{closetId}")
    public ResponseEntity<SuccessResponse> deleteCloset(@AuthenticationPrincipal User user, @PathVariable("closetId") Long closetId){
        closetService.deleteCloset(user, closetId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*옷장에 아이템 스크랩(저장하기)",
            description = """ 
                    사용자 옷장에 아이템을 스크랩(저장하기)
                    User Id Token 필요 -> 해당 옷장의 소유자인지 확인
                    ** 일단 1개의 아이템은 유저당 1개의 옷장에만 저장 가능**
                    A유저가 가지고 있는 1번 2번 옷장에 저장 불가. 1번 혹은 2번에만 저장 가능.
                    """
    )
    @PostMapping("/{itemId}/scrap/{closetId}")
    public ResponseEntity<SuccessResponse> postItemScrapToCloset(@AuthenticationPrincipal User user, @PathVariable("itemId") Long itemId, @PathVariable("closetId") Long closetId){
        closetService.postItemScrapToCloset(user, itemId, closetId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*옷장에 편집하기로 선택한 Item들을 삭제",
            description = """ 
                    옷장 편집하기에서 선택한 Item들을 한번애 삭제하는 기능
                    User Id Token 필요
                    """
    )
    @PatchMapping("/{closetId}/items")
    public ResponseEntity<SuccessResponse> patchItems(@AuthenticationPrincipal User user, @PathVariable("closetId") Long closetId, @RequestBody ClosetItemDeleteReqDto dto){
        closetService.patchItems(user, closetId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*옷장에 편집하기로 선택한 Item들을 삭제",
            description = """ 
                    옷장 편집하기에서 선택한 Item들을 한번애 삭제하는 기능
                    User Id Token 필요
                    """
    )
    @DeleteMapping("/{itemId}/scrap")
    public ResponseEntity<SuccessResponse> deleteItemScrapFromCloset(@AuthenticationPrincipal User user, @PathVariable("itemId") Long itemId){
        closetService.deleteItemScrapFromCloset(user, itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}