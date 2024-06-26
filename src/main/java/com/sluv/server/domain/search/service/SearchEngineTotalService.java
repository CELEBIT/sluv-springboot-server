package com.sluv.server.domain.search.service;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.search.dto.SearchTotalResDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchEngineTotalService {
    private final SearchEngineService searchEngineService;
    private final SearchService searchService;

    /**
     * 토탈 검색 with ElasticSearch
     */
    public SearchTotalResDto getSearchTotal(User user, String keyword) throws ExecutionException, InterruptedException {
        final int itemSize = 9;
        final int questionSize = 4;
        final int userSize = 10;

        // Item 검색
        Pageable itemPageable = PageRequest.of(0, itemSize);
        SearchFilterReqDto dto = SearchFilterReqDto.builder().build();

        // Question 검색 -> 찾아주세요 -> 이거 어때 -> 이 중에 뭐 살까 -> 추천해 줘 순서
        Pageable questionPageable = PageRequest.of(0, questionSize);
        CompletableFuture<PaginationResDto<ItemSimpleResDto>> searchItem = searchEngineService.getSearchItem(user,
                keyword,
                dto, itemPageable);
        CompletableFuture<PaginationResDto<QuestionSimpleResDto>> questionFind = searchEngineService.getSearchQuestion(
                user,
                keyword, "Find", questionPageable);
        CompletableFuture<PaginationResDto<QuestionSimpleResDto>> questionHow = searchEngineService.getSearchQuestion(
                user,
                keyword, "How", questionPageable);
        CompletableFuture<PaginationResDto<QuestionSimpleResDto>> questionBuy = searchEngineService.getSearchQuestion(
                user,
                keyword, "Buy", questionPageable);
        CompletableFuture<PaginationResDto<QuestionSimpleResDto>> questionRecommend = searchEngineService.getSearchQuestion(
                user, keyword, "Recommend", questionPageable);
//        List<QuestionSimpleResDto> result = searchService.getSearchQuestion(user, keyword, "Find", questionPageable).get().getContent().stream().toList();

        // User 검색
        Pageable userPageable = PageRequest.of(0, userSize);

        CompletableFuture<PaginationResDto<UserSearchInfoDto>> searchUser = searchEngineService.getSearchUser(user,
                keyword,
                userPageable);

        CompletableFuture.allOf(searchItem, questionFind, questionHow, questionBuy, questionRecommend, searchUser)
                .join();

        List<QuestionSimpleResDto> result = questionFind.get().getContent();

        if (result.size() < 4) {
            List<QuestionSimpleResDto> temp =
                    questionHow.get().getContent();

            result = Stream.concat(result.stream(), temp.stream()).toList();

        }

        if (result.size() < 4) {
            List<QuestionSimpleResDto> temp =
                    questionBuy.get().getContent();
            result = Stream.concat(result.stream(), temp.stream()).toList();

        }

        if (result.size() < 4) {
            List<QuestionSimpleResDto> temp =
                    questionRecommend.get().getContent();
            result = Stream.concat(result.stream(), temp.stream()).toList();
        }

        List<QuestionSimpleResDto> resultQuestion = result;
        List<ItemSimpleResDto> resultItem = searchItem.get().getContent();
        List<UserSearchInfoDto> resultUser = searchUser.get().getContent();

        // 최근 검색 등록
        searchService.postRecentSearch(user, keyword);

        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return SearchTotalResDto.of(resultItem, resultQuestion, resultUser);
    }

}
