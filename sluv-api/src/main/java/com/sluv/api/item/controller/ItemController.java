package com.sluv.api.item.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.item.dto.ItemDetailResDto;
import com.sluv.api.item.dto.ItemPostReqDto;
import com.sluv.api.item.dto.ItemPostResDto;
import com.sluv.api.item.service.ItemService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.exception.StandardNotFoundException;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    /**
     * 아이템 상세조회
     */
    @Operation(summary = "*특정 아이템 게시글 상세조회", description = "User 토큰 필요")
    @GetMapping("/{itemId}")
    public ResponseEntity<SuccessDataResponse<ItemDetailResDto>> getItemDetail(@CurrentUserId Long userId,
                                                                               @PathVariable("itemId") Long itemId) {
        ItemDetailResDto response = itemService.getItemDetail(userId, itemId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 같은 셀럽 아이템 리스트 -> 10개 같은 브랜드 아이템 리스트 -> 10개
     */
    @Operation(summary = "*같은 셀럽 아이템 리스트 조회", description = "User 토큰 필요")
    @GetMapping("/sameCelebItem")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getSameCelebItem(
            @CurrentUserId Long userId, @RequestParam("itemId") Long itemId) {
        List<ItemSimpleDto> response = itemService.getSameCelebItems(userId, itemId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 같은 브랜드의 아이템 -> 브랜드를 기준으로 최신 아이템 검색 -> 10개
     */
    @Operation(summary = "*같은 브랜드의 아이템 리스트 조회", description = "User 토큰 필요")
    @GetMapping("/sameBrandItem")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getSameBrandItem(
            @CurrentUserId Long userId, @RequestParam("itemId") Long itemId) {
        List<ItemSimpleDto> response = itemService.getSameBrandItem(userId, itemId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 함께 스크랩한 아이템 -> 10개
     */
    @Operation(summary = "*함께 스크랩한 아이템 리스트 조회", description = "User 토큰 필요")
    @GetMapping("/togetherScrap")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getTogetherScrapItem(
            @CurrentUserId Long userId,
            @RequestParam("itemId") Long itemId) {
        List<ItemSimpleDto> response = itemService.getTogetherScrapItem(userId, itemId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*최근 본 아이템 조회", description = "User 토큰 필요. Pagination 적용. 최근 본 순서로 정렬.")
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getRecentItem(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<ItemSimpleDto> response = itemService.getRecentItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*찜한 아이템 조회", description = "User 토큰 필요. Pagination 적용. 스크랩한 최신순으로 정렬.")
    @GetMapping("/scrap")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getScrapItem(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<ItemSimpleDto> response = itemService.getScrapItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*아이템 등록 및 수정", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<ItemPostResDto>> postItem(@CurrentUserId Long userId,
                                                                        @RequestBody ItemPostReqDto reqDto) {
        ItemPostResDto response = itemService.postItem(userId, reqDto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "아이템 게시글 좋아요", description = "User 토큰 필요")
    @PostMapping("/{itemId}/like")
    public ResponseEntity<SuccessResponse> postItemLike(@CurrentUserId Long userId,
                                                        @PathVariable("itemId") Long itemId) {
        itemService.postItemLike(userId, itemId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "아이템 게시글 삭제")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    /* 홈페이지 기능 */

    /**
     * 초반: 좋아요 수, 스크랩 수, 조회수를 모두 합산하여 높은 순서 + 착용 최신순으로 정렬 후반: 유저별 검색기록, 관심셀럽을 기준으로 조회
     */
    @Operation(summary = "*유저별 추천 인기 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getRecommendItem(
            @CurrentUserId Long userId, Pageable pageable) {
        log.info("유저별 추천 인기 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getRecommendItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*핫한 셀럽들이 선택한 여름나기 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Ordering -> 최신순, 인기순, 저가순, 고가순. Filtering -> Price, Color.")
    @GetMapping("/summer")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getSummerItem(
            @CurrentUserId Long userId,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("핫한 셀럽들이 선택한 여름나기 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getSummerItem(userId, pageable, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*지금 당장 구매가능한 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Filtering -> Category, Price, Color")
    @GetMapping("/nowBuy")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getNowBuyItem(
            @CurrentUserId Long userId,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("지금 당장 구매가능한 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getNowBuyItem(userId, pageable, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*실시간 뉴 아이템 조회", description = "WhenDiscovery 기준 아이템 조회. User 토큰 필요. Pagination 적용.")
    @GetMapping("/new")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getNewItem(
            @CurrentUserId Long userId,
            Pageable pageable) {
        log.info("최신 등록 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getNewItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*주목해야할 럭셔리 무드 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/luxury")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getLuxuryItem(
            @CurrentUserId Long userId,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("주목해야할 럭셔리 무드 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getLuxuryItem(userId, pageable, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*가성비 좋은 선물 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/efficient")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getEfficientItem(
            @CurrentUserId Long userId,
            Pageable pageable,
            SearchFilterReqDto filterReqDto) {
        PaginationResponse<ItemSimpleDto> response = itemService.getEfficientItem(userId, pageable, filterReqDto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Deprecated
    @Operation(summary = "*일간/주간 HOT 아이템 조회", description = "User 토큰 필요. 정적으로 21개 조회.")
    @GetMapping("/hotItem")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getHotItem(@CurrentUserId Long userId,
                                                                               @RequestParam("standard") String standard) {
        log.info("일간 주간 핫 아이템 조회: {}", standard);
        List<ItemSimpleDto> result;
        if (standard.equals("주간")) {
            result = itemService.getWeekHotItem(userId);
        } else if (standard.equals("일간")) {
            result = itemService.getDayHotItem(userId);
        } else {
            throw new StandardNotFoundException();
        }

        return ResponseEntity.ok().body(SuccessDataResponse.create(result));
    }

    /**
     * 셀럽 변경은 백오피스 개발시 다시 적용 예정
     */
    @Operation(summary = "*요즘 Hot Celeb의 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Filtering 적용. Ordering 적용.")
    @GetMapping("/hotCeleb")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getHotCelebItem(
            @CurrentUserId Long userId, Pageable pageable, SearchFilterReqDto dto) {
        log.info("요즘 핫셀럽 아이템 조회");
        PaginationResponse<ItemSimpleDto> response = itemService.getHotCelebItem(userId, pageable, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 현재 실시간 -> 24시간 기준 추가 예정 10개가 안되면 최신순으로 랜덤으로 채우기
     */
    @Operation(summary = "*큐레이션 아이템 조회",
            description = "오늘 올라온 관심 셀럽의 아이템을 인기 순으로 검색. User 토큰 필요. 정적으로 10개.")
    @GetMapping("/curation")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getCurationItem(
            @CurrentUserId Long userId) {
        log.info("큐레이션 아이템 조회");
        List<ItemSimpleDto> response = itemService.getCurationItem(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 전체 아이템 중 랜덤으로 4개 조회, 홈 회면 접속 할 때마다 리프레쉬
     */
    @Operation(summary = "*이 아이템은 어때요 아이템 조회", description = "User 토큰 필요.")
    @GetMapping("/howabout")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleDto>>> getHowAboutItem(
            @CurrentUserId Long userId) {
        log.info("이 아이템은 어때요 아이템 조회");
        List<ItemSimpleDto> response = itemService.getHowAboutItem(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
