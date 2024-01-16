package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemDetailResDto;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.dto.ItemPostResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.exception.StandardNotFoundException;
import com.sluv.server.domain.item.service.ItemService;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    /**
     * 같은 셀럽 아이템 리스트 -> 10개 같은 브랜드 아이템 리스트 -> 10개 다른 스러버들이 함께 보관한 아이템 리스트 -> 10개 1. 같은 셀럽의 아이템 -> 셀럽을 기준으로 최신 아이템 검색 2.
     * 같은 브랜드의 아이템 -> 브랜드를 기준으로 최신 아이템 검색 3. 함께 스크랩한 아이템 -> a. 해당 아이템을 저장한 Closet을 최신순으로 20개 추출 b. 20개의 Closet의 모든 Item을
     * 최신순으로 정렬 c. 정렬된 것중 상위 10개 추출.
     */
    @Operation(summary = "*특정 아이템 게시글 상세조회", description = "User 토큰 필요")
    @GetMapping("/{itemId}")
    public ResponseEntity<SuccessDataResponse<ItemDetailResDto>> getItemDetail(@AuthenticationPrincipal User user,
                                                                               @PathVariable("itemId") Long itemId) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ItemDetailResDto>builder()
                        .result(itemService.getItemDetail(user, itemId))
                        .build()
        );
    }

    @Operation(summary = "*최근 본 아이템 조회", description = "User 토큰 필요. Pagination 적용. 최근 본 순서로 정렬.")
    @GetMapping("/recent")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getRecentItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getRecentItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*찜한 아이템 조회", description = "User 토큰 필요. Pagination 적용. 스크랩한 최신순으로 정렬.")
    @GetMapping("/scrap")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getScrapItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getScrapItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*아이템 등록 및 수정", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<ItemPostResDto>> postItem(@AuthenticationPrincipal User user,
                                                                        @RequestBody ItemPostReqDto reqDto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<ItemPostResDto>builder()
                        .result(itemService.postItem(user, reqDto))
                        .build()
        );
    }

    @Operation(summary = "아이템 게시글 좋아요", description = "User 토큰 필요")
    @PostMapping("/{itemId}/like")
    public ResponseEntity<SuccessResponse> postItemLike(@AuthenticationPrincipal User user,
                                                        @PathVariable("itemId") Long itemId) {
        itemService.postItemLike(user, itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "아이템 게시글 삭제")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    /* 홈페이지 기능 */

    /**
     * 초반: 좋아요 수, 스크랩 수, 조회수를 모두 합산하여 높은 순서 + 착용 최신순으로 정렬 후반: 유저별 검색기록, 관심셀럽을 기준으로 조회
     */
    @Operation(summary = "*유저별 추천 인기 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recommend")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getRecommendItem(
            @AuthenticationPrincipal User user, Pageable pageable) {
        log.info("유저별 추천 인기 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getRecommendItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*핫한 셀럽들이 선택한 여름나기 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Ordering -> 최신순, 인기순, 저가순, 고가순. Filtering -> Price, Color.")
    @GetMapping("/summer")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getSummerItem(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("핫한 셀럽들이 선택한 여름나기 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getSummerItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(summary = "*지금 당장 구매가능한 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Filtering -> Category, Price, Color")
    @GetMapping("/nowBuy")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getNowBuyItem(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("지금 당장 구매가능한 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getNowBuyItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(summary = "*최신 등록 아이템 조회", description = "1시간 동안 최신 등록된 아이템 조회. User 토큰 필요. Pagination 적용.")
    @GetMapping("/new")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getNewItem(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        log.info("최신 등록 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getNewItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*주목해야할 럭셔리 무드 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/luxury")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getLuxuryItem(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            SearchFilterReqDto dto) {
        log.info("주목해야할 럭셔리 무드 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getLuxuryItem(user, pageable, dto))
                        .build()
        );
    }

    @Operation(summary = "*가성비 좋은 선물 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/efficient")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getEfficientItem(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            SearchFilterReqDto filterReqDto) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getEfficientItem(user, pageable, filterReqDto))
                        .build()
        );
    }

    @Operation(summary = "*일간/주간 HOT 아이템 조회", description = "User 토큰 필요. 정적으로 21개 조회.")
    @GetMapping("/hotItem")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleResDto>>> getHotItem(@AuthenticationPrincipal User user,
                                                                                  @RequestParam("standard") String standard) {
        log.info("일간 주간 핫 아이템 조회: {}", standard);
        List<ItemSimpleResDto> result;
        if (standard.equals("주간")) {
            result = itemService.getWeekHotItem(user);
        } else if (standard.equals("일간")) {
            result = itemService.getDayHotItem(user);
        } else {
            throw new StandardNotFoundException();
        }

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemSimpleResDto>>builder()
                        .result(result)
                        .build()
        );
    }

    /**
     * 셀럽 변경은 백오피스 개발시 다시 적용 예정
     */
    @Operation(summary = "*요즘 Hot Celeb의 아이템 조회",
            description = "User 토큰 필요. Pagination 적용. Filtering 적용. Ordering 적용.")
    @GetMapping("/hotCeleb")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getHotCelebItem(
            @AuthenticationPrincipal User user, Pageable pageable, SearchFilterReqDto dto) {
        log.info("요즘 핫셀럽 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(itemService.getHotCelebItem(user, pageable, dto))
                        .build()
        );
    }

    /**
     * 현재 실시간 -> 24시간 기준 추가 예정 10개가 안되면 최신순으로 랜덤으로 채우기
     */
    @Operation(summary = "*큐레이션 아이템 조회",
            description = "오늘 올라온 관심 셀럽의 아이템을 인기 순으로 검색. User 토큰 필요. 정적으로 10개.")
    @GetMapping("/curation")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleResDto>>> getCurationItem(
            @AuthenticationPrincipal User user) {
        log.info("큐레이션 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemSimpleResDto>>builder()
                        .result(itemService.getCurationItem(user))
                        .build()
        );
    }

    /**
     * 현재 실시간 -> 24시간 기준 추가 예정 10개가 안되면 최신순으로 랜덤으로 채우기
     */
    @Operation(summary = "*이 아이템은 어때요 아이템 조회", description = "User 토큰 필요. 정적으로 4개. 24시간 기준.")
    @GetMapping("/howabout")
    public ResponseEntity<SuccessDataResponse<List<ItemSimpleResDto>>> getHowAboutItem(
            @AuthenticationPrincipal User user) {
        log.info("이 아이템은 어때요 아이템 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemSimpleResDto>>builder()
                        .result(itemService.getHowAboutItem(user))
                        .build()
        );
    }
}
