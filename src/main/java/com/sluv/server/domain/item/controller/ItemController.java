package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.*;
import com.sluv.server.domain.item.exception.StandardNotFoundException;
import com.sluv.server.domain.item.service.*;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.*;
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
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<TempItemResDto>>> getTempItemList(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<TempItemResDto>>builder()
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
                    
                    1. 같은 셀럽의 아이템 -> 셀럽을 기준으로 최신 아이템 검색
                    2. 같은 브랜드의 아이템 -> 브랜드를 기준으로 최신 아이템 검색
                    3. 함께 스크랩한 아이템 
                    -> 1. 해당 아이템을 저장한 Closet을 최신순으로 20개 추출
                       2. 20개의 Closet의 모든 Item을 최신순으로 정렬
                       3. 정렬된 것중 상위 10개 추출.
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
                    - 최근 본 순서로 정렬
                    """
    )
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getRecentItem(@AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getRecentItem(user, pageable))
                        .build()
        );
    }
    @Operation(
            summary = "*찜한 아이템 조회",
            description = """
                    유저가 찜한 아이템 조회
                    - (User Id 필요)
                    - Pagination 적용
                    - 스크랩한 최신순으로 정렬
                    """
    )
    @GetMapping("/scrap")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getScrapItem(@AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getScrapItem(user, pageable))
                        .build()
        );
    }
    @Operation(
            summary = "*유저별 추천 인기 아이템 조회",
            description = """
                    유저별로 추천 인기 아이템을 조회
                    - (User Id 필요)
                    - Pagination 적용
                    - 초반: 좋아요 수, 스크랩 수, 조회수를 모두 합산하여 높은 순서 + 착용 최신순으로 정렬
                      후반: 유저별 검색기록, 관심셀럽을 기준으로 조회 
                    """
    )
    @GetMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getRecommendItem(@AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getRecommendItem(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "*핫한 셀럽들이 선택한 여름나기 아이템 조회",
            description = """
                    핫한 셀럽들이 선택한 여름나기 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 적용\n
                    - Ordering -> 최신순, 인기순, 저가순, 고가순\n
                    - Filtering -> Price, Color
                    """
    )
    @GetMapping("/summer")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getSummerItem(@AuthenticationPrincipal User user,
                                                                                                 Pageable pageable,
                                                                                                 SearchFilterReqDto dto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getSummerItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*지금 당장 구매가능한 아이템 조회",
            description = """
                    지금 당장 구매가능한 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 적용\n
                    - Filtering -> Category, Price, Color
                    """
    )
    @GetMapping("/nowBuy")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getNowBuyItem(@AuthenticationPrincipal User user,
                                                                                                 Pageable pageable,
                                                                                                 SearchFilterReqDto dto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getNowBuyItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*최신 등록 아이템 조회",
            description = """
                    1시간 동안 최신 등록된 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 적용\n
                    """
    )
    @GetMapping("/new")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getNewItem(@AuthenticationPrincipal User user,
                                                                                                 Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getNewItem(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "*주목해야할 럭셔리 무드 아이템 조회",
            description = """
                    주목해야할 럭셔리 무드 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 적용\n
                    """
    )
    @GetMapping("/luxury")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getLuxuryItem(@AuthenticationPrincipal User user,
                                                                                              Pageable pageable,
                                                                                              SearchFilterReqDto dto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getLuxuryItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*가성비 좋은 선물 아이템 조회",
            description = """
                    가성비 좋은 선물 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 적용\n
                    """
    )
    @GetMapping("/efficient")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getEfficientItem(@AuthenticationPrincipal User user,
                                                                                                 Pageable pageable,
                                                                                                 SearchFilterReqDto dto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getEfficientItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*일간/주간 HOT 아이템 조회",
            description = """
                    *일간/주간 HOT 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - 정적으로 21개 조회.
                    """
    )
    @GetMapping("/hotItem")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleResDto>>> getHotItem(@AuthenticationPrincipal User user,
                                                                                  @RequestParam("standard") String standard) {

        List<ItemSimpleResDto> result;
        if(standard.equals("주간")){
            result = itemService.getWeekHotItem(user);
        } else if (standard.equals("일간")) {
            result = itemService.getDayHotItem(user);
        }else{
            throw new StandardNotFoundException();
        }

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemSimpleResDto>>builder()
                        .result(result)
                        .build()
        );
    }

    @Operation(
            summary = "*요즘 Hot Celeb의 아이템 조회",
            description = """
                    Hot Celeb의 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - Pagination 추가\n
                    - Filtering, Ordering 추가\n
                    ===============\n
                    - 셀럽 변경은 백오피스 개발시 다시 적용 예정
                    """
    )
    @GetMapping("/hotCeleb")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getHotCelebItem(@AuthenticationPrincipal User user, Pageable pageable, SearchFilterReqDto dto) {


        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getHotCelebItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(
            summary = "*큐레이션 아이템 조회",
            description = """
                    큐레이션 아이템 조회\n
                    - (User Id 필요) -> 스크랩 여부 확인\n
                    - 정적을 10개\n
                    - 오늘 올라온 관심 셀럽의 아이템을 인기 순으로 검색
                    - 10개가 안되면 최신순으로 랜덤으로 채우기\n
                    ===============\n
                    - 현재 실시간 -> 24시간 기준 추가 예정
                    """
    )
    @GetMapping("/curation")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleResDto>>> getCurationItem(@AuthenticationPrincipal User user) {


        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemSimpleResDto>>builder()
                        .result(itemService.getCurationItem(user))
                        .build()
        );
    }
}
