package com.sluv.api.search.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.search.dto.RecentSearchChipResDto;
import com.sluv.api.search.dto.SearchItemCountResDto;
import com.sluv.api.search.dto.SearchKeywordResDto;
import com.sluv.api.search.dto.SearchKeywordTotalResDto;
import com.sluv.api.search.dto.SearchTotalResDto;
import com.sluv.api.search.service.SearchEngineService;
import com.sluv.api.search.service.SearchEngineTotalService;
import com.sluv.api.search.service.SearchService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final SearchEngineService searchEngineService;
    private final SearchEngineTotalService searchEngineTotalService;

    @Operation(
            summary = "*Item 검색",
            description = """
                    Keyword로 Item 검색 with ElasticSearch \n
                    - Pagination 적용
                    - User Id Token 필요
                      -> Scrap 여부 판단을 위해 필요
                    """
    )
    @GetMapping("/item")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> searchItem(
            @CurrentUserId Long userId,
            @RequestParam("keyword") String keyword,
            SearchFilterReqDto dto,
            Pageable pageable) throws ExecutionException, InterruptedException {

        PaginationResponse<ItemSimpleDto> response = searchEngineService.getSearchItem(userId,
                keyword, dto, pageable).get();

        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "*Question 검색",
            description = """
                    Keyword로 Question 검색 with ElasticSearch \n
                    - Pagination 적용
                    - User Id Token 필요
                      -> 필요 없지만 일관성을 위해 필요 \n
                    - qtype : [Buy, Find, How, Recommend] \n
                        -> 전체검색 시 qtype = null
                    """
    )
    @GetMapping("/question")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<QuestionSimpleResDto>>> searchQuestion(
            @CurrentUserId Long userId,
            @RequestParam("keyword") String keyword,
            @Nullable @RequestParam("qtype") String qType,
            Pageable pageable) throws ExecutionException, InterruptedException {

        PaginationResponse<QuestionSimpleResDto> response = searchEngineService.getSearchQuestion(
                userId, keyword, qType, pageable).get();

        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "*User 검색",
            description = """
                    Keyword로 User 검색 with ElasticSearch \n
                    - Pagination 적용
                    - User Id Token 필요
                      -> 팔로우 여부 판단을 위해 필요
                    """
    )
    @GetMapping("/user")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserSearchInfoDto>>> searchUser(
            @CurrentUserId Long userId,
            @RequestParam("keyword") String keyword,
            Pageable pageable) throws ExecutionException, InterruptedException {
        PaginationResponse<UserSearchInfoDto> response = searchEngineService.getSearchUser(
                userId, keyword, pageable).get();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "*Total 검색",
            description = """
                    Keyword로 Total 검색 with ElasticSearch \n
                    - Pagination 적용
                    - User Id Token 필요
                      -> Item Scrap 여부, User 팔로우 여부를 판단하기 위해 필요
                    """
    )
    @GetMapping("/total")
    public ResponseEntity<SuccessDataResponse<SearchTotalResDto>> searchTotal(@CurrentUserId Long userId,
                                                                              @RequestParam("keyword") String keyword)
            throws ExecutionException, InterruptedException {
        SearchTotalResDto response = searchEngineTotalService.getSearchTotal(userId, keyword);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "필터링 조건에 따른 아이템 개수 조회",
            description = """
                    필터링 조건에 맞는 아이템 개수를 조회하는 기능 \n
                    User Id Token 필요
                    """
    )
    @GetMapping("/item/count")
    public ResponseEntity<SuccessDataResponse<SearchItemCountResDto>> searchItemCount(
            @RequestParam("keyword") String keyword, SearchFilterReqDto dto) {
        SearchItemCountResDto response = searchEngineService.getSearchItemCount(keyword, dto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "최근 검색어 조회",
            description = """
                    유저에 따른 최근 검색어 조회 API\n
                    User Id Token 필요
                    """
    )
    @GetMapping("/recentSearch")
    public ResponseEntity<SuccessDataResponse<List<RecentSearchChipResDto>>> getRecentSearch(
            @CurrentUserId Long userId) {
        List<RecentSearchChipResDto> response = searchService.getRecentSearch(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "인기 검색어 조회",
            description = """
                    인기 검색어 조회\n
                    한번에 12개를 모두 조회.\n
                    Scheduler에 의해 12개를 업데이트
                    """
    )
    @GetMapping("/searchRank")
    public ResponseEntity<SuccessDataResponse<List<SearchKeywordResDto>>> getRecentSearch() {
        List<SearchKeywordResDto> response = searchService.getSearchRank();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "Keyword 자동 완성",
            description = """
                    Search Data에 있는 것을 기반으로 자동완성 검색.\n
                    Pagination 적용
                    """
    )
    @GetMapping("/keyword")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<SearchKeywordResDto>>> getSearchKeyword(
            @RequestParam("keyword") String keyword,
            Pageable pageable) {
        PaginationResponse<SearchKeywordResDto> response = searchService.getSearchKeyword(keyword, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "*최근 검색어 삭제 ",
            description = """
                    현재 유저의 최근 검색어 삭제 API.\n
                    User Id Token 필요
                    """
    )
    @DeleteMapping("/recentSearch")
    public ResponseEntity<SuccessResponse> deleteRecentSearch(@CurrentUserId Long userId,
                                                              @RequestParam String keyword) {
        searchService.deleteSearchKeyword(userId, keyword);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(
            summary = "*최근 검색어 모두 삭제 ",
            description = """
                    현재 유저의 최근 검색어를 모두 삭제하는 API.\n
                    User Id Token 필요
                    """
    )
    @DeleteMapping("/recentSearch/all")
    public ResponseEntity<SuccessResponse> deleteAllRecentSearch(@CurrentUserId Long userId) {
        searchService.deleteAllSearchKeyword(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*검색어 기준 브랜드, 셀럽, 아이템 검색 ", description = "각 5개씩")
    @GetMapping("/allData")
    public ResponseEntity<SuccessDataResponse<SearchKeywordTotalResDto>> getAllDateByKeyword(
            @CurrentUserId Long userId,
            @RequestParam("keyword") String keyword) {
        SearchKeywordTotalResDto response = searchService.getAllDateByKeyword(userId, keyword);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
