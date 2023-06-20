package com.sluv.server.domain.search.controller;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.search.dto.*;
import com.sluv.server.domain.search.service.SearchService;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

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
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> searchItem(@AuthenticationPrincipal User user,
                                                                         @RequestParam("keyword") String keyword,
                                                                         SearchFilterReqDto dto,
                                                                         Pageable pageable){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(searchService.getSearchItem(user, keyword, dto, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "*Question 검색",
            description = """
                    Keyword로 Question 검색 with ElasticSearch \n
                    - Pagination 적용
                    - User Id Token 필요
                      -> 필요 없지만 일관성을 위해 필요
                    """
    )
    @GetMapping("/question")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<QuestionSimpleResDto>>> searchQuestion(@AuthenticationPrincipal User user,
                                                                                 @RequestParam("keyword") String keyword,
                                                                                 @RequestParam("qtype") String qType,
                                                                                 Pageable pageable){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<QuestionSimpleResDto>>builder()
                            .result(searchService.getSearchQuestion(user, keyword, qType, pageable))
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
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> searchUser(@AuthenticationPrincipal User user,
                                                                          @RequestParam("keyword") String keyword,
                                                                          Pageable pageable){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                                .result(searchService.getSearchUser(user, keyword, pageable))
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
                                                                              @RequestParam("keyword") String keyword){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SearchTotalResDto>builder()
                        .result(searchService.getSearchTotal(user, keyword))
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
    public ResponseEntity<SuccessDataResponse<SearchItemCountResDto>> searchItemCount(@RequestParam("keyword") String keyword, SearchFilterReqDto dto){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SearchItemCountResDto>builder()
                        .result(searchService.getSearchItemCount(keyword, dto))
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
    public ResponseEntity<SuccessDataResponse<List<RecentSearchChipResDto>>> getRecentSearch(@AuthenticationPrincipal User user){
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
                    한번에 9개를 모두 조회.
                    """
    )
    @GetMapping("/searchRank")
    public ResponseEntity<SuccessDataResponse<List<SearchRankResDto>>> getRecentSearch(){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<SearchRankResDto>>builder()
                        .result(searchService.getSearchRank())
                        .build()
        );
    }
}
