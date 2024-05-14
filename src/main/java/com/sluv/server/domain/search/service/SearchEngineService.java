package com.sluv.server.domain.search.service;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.question.service.QuestionService;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.search.dto.SearchItemCountResDto;
import com.sluv.server.domain.search.engine.SearchEngine;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchEngineService {

    private final SearchEngine searchEngine;

    private final SearchService searchService;
    private final QuestionService questionService;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResDto<ItemSimpleResDto>> getSearchItem(User user, String keyword,
                                                                               SearchFilterReqDto dto,
                                                                               Pageable pageable) {

        List<Long> searchItemIds = searchEngine.getSearchItemIds(keyword);
        Page<Item> searchItemPage = itemRepository.getSearchItem(searchItemIds, dto, pageable);
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, searchItemPage.getContent());

        // 최근 검색 등록
        searchService.postRecentSearch(user, keyword);
        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResDto.of(searchItemPage, content));
    }

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResDto<QuestionSimpleResDto>> getSearchQuestion(User user, String keyword,
                                                                                       String qType,
                                                                                       Pageable pageable) {
        List<Long> searchQuestionIds = searchEngine.getSearchQuestionIds(keyword);
        Page<?> searchQuestionPage;
        // 조건에 맞는 Item Page 조회
        if (qType == null) {
            searchQuestionPage =
                    questionRepository.getSearchQuestion(searchQuestionIds, pageable);
        } else if (qType.equals("Buy")) {
            searchQuestionPage =
                    questionRepository.getSearchQuestionBuy(searchQuestionIds, pageable);
        } else if (qType.equals("Find")) {
            searchQuestionPage =
                    questionRepository.getSearchQuestionFind(searchQuestionIds, pageable);
        } else if (qType.equals("How")) {
            searchQuestionPage =
                    questionRepository.getSearchQuestionHowabout(searchQuestionIds, pageable);
        } else if (qType.equals("Recommend")) {
            searchQuestionPage =
                    questionRepository.getSearchQuestionRecommend(searchQuestionIds, pageable);
        } else {
            throw new QuestionTypeNotFoundException();
        }

        List<QuestionSimpleResDto> content = searchQuestionPage.stream()
                .map(question ->
                        questionService.getQuestionSimpleResDto((Question) question, qType)
                ).toList();

        // 최근 검색 등록
        searchService.postRecentSearch(user, keyword);

        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResDto.of(searchQuestionPage, content));

    }

    @Async(value = "asyncThreadPoolExecutor")
    public CompletableFuture<PaginationResDto<UserSearchInfoDto>> getSearchUser(User user, String keyword,
                                                                                Pageable pageable) {
        List<Long> searchUserIds = searchEngine.getSearchUserIds(keyword);
        Page<User> searchUserPage = userRepository.getSearchUser(searchUserIds, pageable);
        List<UserSearchInfoDto> content = searchUserPage.stream().map(searchUser ->
                UserSearchInfoDto.of(searchUser, followRepository.getFollowStatus(user, searchUser.getId()),
                        Objects.equals(searchUser.getId(), user.getId()))
        ).toList();

        // 최근 검색 등록
        searchService.postRecentSearch(user, keyword);
        // 서치 데이터 등록
        searchService.postSearchData(keyword);

        return CompletableFuture.completedFuture(PaginationResDto.of(searchUserPage, content));
    }

    @Transactional
    public SearchItemCountResDto getSearchItemCount(String keyword, SearchFilterReqDto dto) {
        List<Long> itemIdList = searchEngine.getSearchItemIds(keyword);
        return SearchItemCountResDto.of(itemRepository.getSearchItemCount(itemIdList, dto));
    }
}
