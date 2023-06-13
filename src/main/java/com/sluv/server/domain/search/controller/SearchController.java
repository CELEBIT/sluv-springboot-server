package com.sluv.server.domain.search.controller;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.search.service.SearchService;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PaginationResDto<ItemSimpleResDto>> searchItem(@AuthenticationPrincipal User user,
                                                                         @RequestParam("keyword") String keyword,
                                                                         Pageable pageable){
        return ResponseEntity.ok().body(
                searchService.getSearchItem(user, keyword, pageable)
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
    public ResponseEntity<PaginationResDto<QuestionSimpleResDto>> searchQuestion(@AuthenticationPrincipal User user,
                                                                                 @RequestParam("keyword") String keyword,
                                                                                 @RequestParam("qtype") String qType,
                                                                                 Pageable pageable){
        return ResponseEntity.ok().body(
                searchService.getSearchQuestion(user, keyword, qType, pageable)
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
    public ResponseEntity<PaginationResDto<UserSearchInfoDto>> searchUser(@AuthenticationPrincipal User user,
                                                                          @RequestParam("keyword") String keyword,
                                                                          Pageable pageable){
        return ResponseEntity.ok().body(
                searchService.getSearchUser(user, keyword, pageable)
        );
    }

}
