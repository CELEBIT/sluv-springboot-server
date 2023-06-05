package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.*;
import com.sluv.server.domain.item.repository.ItemReportRepository;
import com.sluv.server.domain.item.service.*;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.PaginationResDto;
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
    private final ItemEditReqService itemEditReqService;
    private final ItemReportService itemReportService;

    @Operation(
            summary = "*아이템 등록 및 수정",
            description = "아이템 등록 요청"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<ItemPostResDto>> postItem(@AuthenticationPrincipal User user, @RequestBody ItemPostReqDto reqDto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ItemPostResDto>builder()
                        .result(itemService.postItem(user, reqDto))
                        .build()
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
    public ResponseEntity<SuccessDataResponse<TempItemPageDto<TempItemResDto>>> getTempItemList(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<TempItemPageDto<TempItemResDto>>builder()
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
            description = """
                    특정 아이템 게시글의 상세조회를 하는 API.
                    (User Token 필요)
                    같은 셀럽 아이템 리스트 -> 10개
                    같은 브랜드 아이템 리스트 -> 10개
                    다른 스러버들이 함께 보관한 아이템 리스트 -> 10개
                    """

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
    @Operation(
            summary = "아이템 게시글 삭제",
            description = "특정 아이템 게시글을 삭제 기능 API."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<SuccessResponse> deleteItemLike(@PathVariable("itemId") Long itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*아이템 게시글 수정 요청",
            description = "유저가 특정 아이템 게시글의 내용을 수정 요청" +
                    " (User Id 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{itemId}/edit-req")
    public ResponseEntity<SuccessResponse> postItemEdit(@AuthenticationPrincipal User user, @PathVariable(name = "itemId") Long itemId, @RequestBody ItemEditReqDto dto) {
        itemEditReqService.postItemEdit(user, itemId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*아이템 게시글 신고",
            description = "유저가 특정 아이템 게시글을 신고" +
                    " (User Id 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{itemId}/report")
    public ResponseEntity<SuccessResponse> postItemReport(@AuthenticationPrincipal User user, @PathVariable(name = "itemId") Long itemId, @RequestBody ItemReportReqDto dto) {
        itemReportService.postItemReport(user, itemId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*최근 본 아이템 조회 ",
            description = """
                    유저가 최근 본 아이템을 조회
                    - (User Id 필요)
                    - Pagination 적용
                    """
    )
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSameResDto>>> getRecentItem(@AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSameResDto>>builder()
                        .result(itemService.getRecentItem(user, pageable))
                        .build()
        );
    }
}
