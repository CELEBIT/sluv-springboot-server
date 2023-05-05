package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.*;
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
import org.springframework.data.domain.Pageable;
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
    private final TempItemService tempItemService;

    @Operation(
            summary = "*아이템 등록",
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
            summary = "*Item 임시저장",
            description = "작성중이 Item을 임시저장"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/temp")
    public ResponseEntity<SuccessDataResponse<TempItemPostResDto>> postTempItem(@AuthenticationPrincipal User user, @RequestBody TempItemPostReqDto reqDto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<TempItemPostResDto>builder()
                        .result(
                                TempItemPostResDto.builder()
                                                .tempItemId(tempItemService.postTempItem(user, reqDto))
                                                .build()
                                )
                        .build()
        );
    }
    @Operation(
            summary = "*임시저장 아이템 리스트 조회",
            description = "사용자의 임시저장 아이템 리스트 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/temp")
    public ResponseEntity<SuccessDataResponse<List<TempItemResDto>>> getTempItemList(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<TempItemResDto>>builder()
                        .result(tempItemService.getTempItemList(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "임시저장 아이템 선택삭제",
            description = "사용자가 선택한 임시저장 아이템 삭제."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/temp/{tempItemId}")
    public ResponseEntity<SuccessResponse> deleteTempItem(@PathVariable Long tempItemId){
        tempItemService.deleteTempItem(tempItemId);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @Operation(
            summary = "*임시저장 아이템 전체삭제",
            description = "사용자의 임시저장 아이템 전체삭제."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/temp")
    public ResponseEntity<SuccessResponse> deleteAllTempItem(@AuthenticationPrincipal User user){
        tempItemService.deleteAllTempItem(user);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @Operation(
            summary = "*특정 아이템 게시글 상세조회",
            description = "특정 아이템 게시글의 상세조회를 하는 API." +
                    " (User Token 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<SuccessDataResponse<ItemDetailResDto>> getItemDetail(@AuthenticationPrincipal User user, @PathVariable("itemId") Long itemId){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ItemDetailResDto>builder()
                        .result(
                                itemService.getItemDetail(user, itemId)
                        )
                        .build()
        );
    }
    @Operation(
            summary = "아이템 게시글 좋아요",
            description = "특정 아이템 게시글에 좋아요, 취소 기능 API." +
                    " (User Token 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{itemId}/like")
    public ResponseEntity<SuccessResponse> postItemLike(@AuthenticationPrincipal User user, @PathVariable("itemId") Long itemId){
        itemService.postItemLike(user, itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

}
