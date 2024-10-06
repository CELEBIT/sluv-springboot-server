package com.sluv.api.search.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.service.QuestionService;
import com.sluv.api.search.dto.SearchItemCountResDto;
import com.sluv.api.search.engine.SearchEngine;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SearchEngineService {

    private final SearchEngine searchEngine;

    private final SearchService searchService;
    private final QuestionService questionService;

    private final ItemDomainService itemDomainService;
    private final UserDomainService userDomainService;
    private final FollowDomainService followDomainService;
    private final QuestionDomainService questionDomainService;

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResponse<ItemSimpleDto>> getSearchItem(Long userId, String keyword,
                                                                              SearchFilterReqDto dto,
                                                                              Pageable pageable) {

        User user = userDomainService.findById(userId);

        List<Long> searchItemIds = searchEngine.getSearchItemIds(keyword);
        Page<Item> searchItemPage = itemDomainService.getSearchItem(searchItemIds, dto, pageable);
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, searchItemPage.getContent());

        // 최근 검색 등록
        searchService.postRecentSearch(userId, keyword);
        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResponse.create(searchItemPage, content));
    }

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResponse<QuestionSimpleResDto>> getSearchQuestion(Long userId, String keyword,
                                                                                         String qType,
                                                                                         Pageable pageable) {
        User user = userDomainService.findById(userId);

        List<Long> searchQuestionIds = searchEngine.getSearchQuestionIds(keyword);
        Page<?> searchQuestionPage;
        // 조건에 맞는 Item Page 조회
        if (qType == null) {
            searchQuestionPage =
                    questionDomainService.getSearchQuestion(searchQuestionIds, pageable);
        } else if (qType.equals("Buy")) {
            searchQuestionPage =
                    questionDomainService.getSearchQuestionBuy(searchQuestionIds, pageable);
        } else if (qType.equals("Find")) {
            searchQuestionPage =
                    questionDomainService.getSearchQuestionFind(searchQuestionIds, pageable);
        } else if (qType.equals("How")) {
            searchQuestionPage =
                    questionDomainService.getSearchQuestionHowabout(searchQuestionIds, pageable);
        } else if (qType.equals("Recommend")) {
            searchQuestionPage =
                    questionDomainService.getSearchQuestionRecommend(searchQuestionIds, pageable);
        } else {
            throw new QuestionTypeNotFoundException();
        }

        List<QuestionSimpleResDto> content = searchQuestionPage.stream()
                .map(question ->
                        questionService.getQuestionSimpleResDto((Question) question, qType)
                ).toList();

        // 최근 검색 등록
        searchService.postRecentSearch(userId, keyword);

        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResponse.create(searchQuestionPage, content));

    }

    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResponse<UserSearchInfoDto>> getSearchUser(Long userId, String keyword,
                                                                                  Pageable pageable) {
        User user = userDomainService.findById(userId);

        List<Long> searchUserIds = searchEngine.getSearchUserIds(keyword);
        Page<User> searchUserPage = userDomainService.getSearchUser(searchUserIds, pageable);
        List<UserSearchInfoDto> content = searchUserPage.stream().map(searchUser ->
                UserSearchInfoDto.of(searchUser, followDomainService.getFollowStatus(user, searchUser.getId()),
                        Objects.equals(searchUser.getId(), user.getId()))
        ).toList();

        // 최근 검색 등록
        searchService.postRecentSearch(userId, keyword);
        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResponse.create(searchUserPage, content));
    }

    @Transactional
    public SearchItemCountResDto getSearchItemCount(String keyword, SearchFilterReqDto dto) {
        List<Long> itemIds = searchEngine.getSearchItemIds(keyword);
        return SearchItemCountResDto.of(itemDomainService.getSearchItemCount(itemIds, dto));
    }
}
