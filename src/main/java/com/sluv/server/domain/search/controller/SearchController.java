package com.sluv.server.domain.search.controller;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.search.dto.RecentSearchChipResDto;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.search.dto.SearchItemCountResDto;
import com.sluv.server.domain.search.dto.SearchKeywordResDto;
import com.sluv.server.domain.search.dto.SearchKeywordTotalResDto;
import com.sluv.server.domain.search.dto.SearchTotalResDto;
import com.sluv.server.domain.search.service.SearchEngineService;
import com.sluv.server.domain.search.service.SearchEngineTotalService;
import com.sluv.server.domain.search.service.SearchService;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> searchItem(
            @AuthenticationPrincipal User user,
            @RequestParam("keyword") String keyword,
            SearchFilterReqDto dto,
            Pageable pageable) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(searchEngineService.getSearchItem(user, keyword, dto, pageable).get())
                        .build()
        );
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
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> searchQuestion(
            @AuthenticationPrincipal User user,
            @RequestParam("keyword") String keyword,
            @Nullable @RequestParam("qtype") String qType,
            Pageable pageable) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                        .result(searchEngineService.getSearchQuestion(user, keyword, qType, pageable).get())
                        .build()
        );
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
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> searchUser(
            @AuthenticationPrincipal User user,
            @RequestParam("keyword") String keyword,
            Pageable pageable) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                        .result(searchEngineService.getSearchUser(user, keyword, pageable).get())
                        .build()
        );
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
    public ResponseEntity<SuccessDataResponse<SearchTotalResDto>> searchTotal(@AuthenticationPrincipal User user,
                                                                              @RequestParam("keyword") String keyword)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SearchTotalResDto>builder()
                        .result(searchEngineTotalService.getSearchTotal(user, keyword))
                        .build()
        );
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
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SearchItemCountResDto>builder()
                        .result(searchEngineService.getSearchItemCount(keyword, dto))
                        .build()
        );
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
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<RecentSearchChipResDto>>builder()
                        .result(searchService.getRecentSearch(user))
                        .build()
        );
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
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<SearchKeywordResDto>>builder()
                        .result(searchService.getSearchRank())
                        .build()
        );
    }

    @Operation(
            summary = "Keyword 자동 완성",
            description = """
                    Search Data에 있는 것을 기반으로 자동완성 검색.\n
                    Pagination 적용
                    """
    )
    @GetMapping("/keyword")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<SearchKeywordResDto>>> getSearchKeyword(
            @RequestParam("keyword") String keyword,
            Pageable pageable) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<SearchKeywordResDto>>builder()
                        .result(searchService.getSearchKeyword(keyword, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "*최근 검색어 삭제 ",
            description = """
                    현재 유저의 최근 검색어 삭제 API.\n
                    User Id Token 필요
                    """
    )
    @DeleteMapping("/recentSearch")
    public ResponseEntity<SuccessResponse> deleteRecentSearch(@AuthenticationPrincipal User user,
                                                              @RequestParam String keyword) {
        searchService.deleteSearchKeyword(user, keyword);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*최근 검색어 모두 삭제 ",
            description = """
                    현재 유저의 최근 검색어를 모두 삭제하는 API.\n
                    User Id Token 필요
                    """
    )
    @DeleteMapping("/recentSearch/all")
    public ResponseEntity<SuccessResponse> deleteAllRecentSearch(@AuthenticationPrincipal User user) {
        searchService.deleteAllSearchKeyword(user);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*검색어 기준 브랜드, 셀럽, 아이템 검색 ", description = "각 5개씩")
    @GetMapping("/allData")
    public ResponseEntity<SuccessDataResponse<SearchKeywordTotalResDto>> getAllDateByKeyword(
            @AuthenticationPrincipal User user,
            @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SearchKeywordTotalResDto>builder()
                        .result(searchService.getAllDateByKeyword(user, keyword))
                        .build()
        );
    }
}
