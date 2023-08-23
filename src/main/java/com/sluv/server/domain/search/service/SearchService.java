package com.sluv.server.domain.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.*;
import com.sluv.server.domain.search.dto.*;
import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.search.entity.SearchData;
import com.sluv.server.domain.search.entity.SearchRank;
import com.sluv.server.domain.search.repository.RecentSearchRepository;
import com.sluv.server.domain.search.repository.SearchDataRepository;
import com.sluv.server.domain.search.repository.SearchRankRepository;
import com.sluv.server.domain.search.utils.ElasticSearchConnectUtil;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final QuestionRepository questionRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final CommentRepository commentRepository;

    private final ClosetRepository closetRepository;
    private final ElasticSearchConnectUtil elasticSearchConnectUtil;
    private final RecentSearchRepository recentSearchRepository;
    private final SearchRankRepository searchRankRepository;
    private final SearchDataRepository searchDataRepository;


    /**
     * Item 검색 with ElasticSearch
     */
    @Transactional
    public PaginationResDto<ItemSimpleResDto> getSearchItem(User user, String keyword, SearchFilterReqDto dto, Pageable pageable) {
        // ElasticSearch API Path
        String itemPath = "/search/searchItem";

        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        // 조건에 맞는 Item Page 조회
        Page<Item> searchItemPage = itemRepository.getSearchItem(itemIdList, dto, pageable);

        // 현재 접속한 User의 옷장 목록 조회
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        // Cotent 조립
        List<ItemSimpleResDto> content = searchItemPage.stream().map(item ->
                ItemSimpleResDto.of(
                        item,
                        itemImgRepository.findMainImg(item.getId()),
                        itemScrapRepository.getItemScrapStatus(item, closetList)
                )
        ).toList();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

        // 서치 데이터 등록
        postSearchData(keyword);

        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(searchItemPage.getNumber())
                .hasNext(searchItemPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * Question 검색 with ElasticSearch
     */
    @Transactional
    public PaginationResDto<QuestionSimpleResDto> getSearchQuestion(User user, String keyword, String qType, Pageable pageable) {
        // ElasticSearch API Path
        String itemPath = "/search/searchQuestion";

        // ElasticSearch 에서 Keyword에 해당하는 Question의 Id 조회
        List<Long> questionIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        // 조건에 맞는 Item Page 조회
        if(qType.equals("Buy")){
            Page<QuestionBuy> searchQuestionPage =
                    questionRepository.getSearchQuestionBuy(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->
            {
                // 해당 Question의 item 이미지 리스트 구하기
                List<QuestionItem> questionItemList = questionItemRepository.findAllByQuestionId(question.getId());
//                List<QuestionImgSimpleResDto> itemImgList = questionItemList.stream().map(this::convertQuestionItemToQuestionImgSimpleResDto).toList();
                List<QuestionImgSimpleResDto> itemImgList = questionItemList.stream()
                                                .map(questionItem -> {
                                                    ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                                                    return QuestionImgSimpleResDto.of(mainImg);
                                                }).toList();

                // 해당 Question의 이미지 리스트 구하기
                List<QuestionImgSimpleResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionImgSimpleResDto::of).toList();

                // 작성자 InfoDto
                UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

                // Question 좋아요 수
                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

                // Question 댓글 수
                Long commentNum = commentRepository.countByQuestionId(question.getId());

                return QuestionSimpleResDto.of("Buy", userInfoDto, likeNum, commentNum,
                        question, null, imgList, itemImgList, null);
            }).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("Find")){
            Page<QuestionFind> searchQuestionPage =
                    questionRepository.getSearchQuestionFind(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question -> {
                String celebName = question.getCeleb() != null
                                    ? question.getCeleb().getCelebNameKr()
                                    : question.getNewCeleb().getCelebName();

                // 작성자 InfoDto
                UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

                // Question 좋아요 수
                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

                // Question 댓글 수
                Long commentNum = commentRepository.countByQuestionId(question.getId());

                return QuestionSimpleResDto.of("Find", userInfoDto, likeNum, commentNum,
                        question, celebName, null, null, null);

            }).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("How")){
            Page<QuestionHowabout> searchQuestionPage =
                    questionRepository.getSearchQuestionHowabout(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question -> {

                    // 작성자 InfoDto
                    UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

                    // Question 좋아요 수
                    Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

                    // Question 댓글 수
                    Long commentNum = commentRepository.countByQuestionId(question.getId());

                    return QuestionSimpleResDto.of("How", userInfoDto, likeNum, commentNum,
                            question, null, null, null, null);
            }).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("Recommend")) {
            Page<QuestionRecommend> searchQuestionPage =
                    questionRepository.getSearchQuestionRecommend(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->
            {
                List<String> categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionRecommendCategory::getName).toList();
                // 작성자 InfoDto
                UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

                // Question 좋아요 수
                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

                // Question 댓글 수
                Long commentNum = commentRepository.countByQuestionId(question.getId());

                return QuestionSimpleResDto.of("Recommend", userInfoDto, likeNum, commentNum,
                        question, null, null, null, categoryList);
            }).toList();

            // 최근 검색 등록
            postRecentSearch(user, keyword);

            // 서치 데이터 등록
            postSearchData(keyword);

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();
        }

        throw new QuestionTypeNotFoundException();
    }

    /**
     * User 검색 with ElasticSearch
     */
    @Transactional
    public PaginationResDto<UserSearchInfoDto> getSearchUser(User user, String keyword, Pageable pageable) {
        // ElasticSearch API Path
        String itemPath = "/search/searchUser";

        // ElasticSearch 에서 Keyword에 해당하는 User의 Id 조회
        List<Long> userIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        // 조건에 맞는 User Page 조회
        Page<User> searchUserPage = userRepository.getSearchUser(userIdList, pageable);


        // Cotent 조립
        List<UserSearchInfoDto> content = searchUserPage.stream().map(searchUser ->
                UserSearchInfoDto.of(
                        searchUser,
                        followRepository.getFollowStatus(user, searchUser)
                )
        ).toList();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

        // 서치 데이터 등록
        postSearchData(keyword);

        return PaginationResDto.<UserSearchInfoDto>builder()
                .page(searchUserPage.getNumber())
                .hasNext(searchUserPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * 토탈 검색 with ElasticSearch
     */
    @Transactional
    public SearchTotalResDto getSearchTotal(User user, String keyword) {
        final int itemSize = 9;
        final int questionSize = 4;
        final int userSize = 10;


        // Item 검색
        Pageable itemPageable = PageRequest.of(0, itemSize);
        SearchFilterReqDto dto = SearchFilterReqDto.builder().build();

        List<ItemSimpleResDto> searchItem = this.getSearchItem(user, keyword, dto, itemPageable).getContent();

        // Question 검색 -> 찾아주세요 -> 이거 어때 -> 이 중에 뭐 살까 -> 추천해 줘 순서
        Pageable questionPageable = PageRequest.of(0, questionSize);

        List<QuestionSimpleResDto> result = this.getSearchQuestion(user, keyword, "Find", questionPageable).getContent().stream().toList();


        if(result.size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "How", questionPageable).getContent();

            result = Stream.concat(result.stream(), temp.stream()).toList();

        }


        if(result.size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "Buy", questionPageable).getContent();
            result = Stream.concat(result.stream(), temp.stream()).toList();

        }

        if(result.size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "Recommend", questionPageable).getContent();
            result = Stream.concat(result.stream(), temp.stream()).toList();
        }

        List<QuestionSimpleResDto> searchQuestion = result;

        // User 검색
        Pageable userPageable = PageRequest.of(0, userSize);

        List<UserSearchInfoDto> searchUser = this.getSearchUser(user, keyword, userPageable).getContent();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

        // 서치 데이터 등록
        postSearchData(keyword);

        return SearchTotalResDto.of(searchItem, searchQuestion, searchUser);
    }

    public SearchItemCountResDto getSearchItemCount(String keyword, SearchFilterReqDto dto) {
        // ElasticSearch API Path
        String itemPath = "/search/searchItem";

        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        return SearchItemCountResDto.of(
                    itemRepository.getSearchItemCount(itemIdList, dto)
                );
    }

    private void postRecentSearch(User user, String keyword){
        RecentSearch recentSearch = RecentSearch.of(user, keyword);

        log.info("Post RecentSearch -> User: {}, Keyword: {}", user.getId(), keyword);
        recentSearchRepository.save(recentSearch);

    }

    public List<RecentSearchChipResDto> getRecentSearch(User user) {
        List<RecentSearchChipResDto> result
                = recentSearchRepository.getRecentSearch(user)
                .stream()
                .map(recentSearch ->
                        RecentSearchChipResDto.of(recentSearch.getSearchWord())
                ).toList();

        return result;
    }

    public List<SearchKeywordResDto> getSearchRank() {
        List<SearchRank> searchRankList = searchRankRepository.findAllByOrderBySearchCountDesc();
        return searchRankList
                .stream()
                .map(searchRank ->
                        SearchKeywordResDto.of(searchRank.getSearchWord())
                ).toList();
    }

    private void postSearchData(String keyword){
        SearchData searchData = SearchData.of(keyword);

        log.info("Post SearchData -> Keyword: {}", keyword);
        searchDataRepository.save(searchData);

    }

    public PaginationResDto<SearchKeywordResDto> getSearchKeyword(String keyword, Pageable pageable) {
        Page<SearchData> searchDataPage = searchDataRepository.getSearchKeyword(keyword, pageable);

        List<SearchKeywordResDto> content = searchDataPage.stream()
                .map(searchData ->
                        SearchKeywordResDto.of(searchData.getSearchWord()
                )
        ).toList();

        return PaginationResDto.<SearchKeywordResDto>builder()
                .page(searchDataPage.getNumber())
                .hasNext(searchDataPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * 현재 유저의 RecentSearch 키워드 삭제
     */
    @Transactional
    public void deleteSearchKeyword(User user, String keyword) {
        log.info("Delete {}'s Recent Search Keyword: {} ", user.getId(), keyword);
        recentSearchRepository.deleteByUserIdAndSearchWord(user.getId(), keyword);
    }
}
