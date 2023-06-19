package com.sluv.server.domain.search.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.question.repository.QuestionItemRepository;
import com.sluv.server.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.search.dto.RecentSearchChipResDto;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.search.dto.SearchItemCountResDto;
import com.sluv.server.domain.search.dto.SearchTotalResDto;
import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.search.repository.RecentSearchRepository;
import com.sluv.server.domain.search.utils.ElasticSearchConnectUtil;
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

    private final ClosetRepository closetRepository;
    private final ElasticSearchConnectUtil elasticSearchConnectUtil;
    private final RecentSearchRepository recentSearchRepository;

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
                ItemSimpleResDto.builder()
                        .itemId(item.getId())
                        .imgUrl(itemImgRepository.findMainImg(item.getId()).getItemImgUrl())
                        .brandName(
                                item.getBrand() != null
                                        ? item.getBrand().getBrandKr()
                                        : item.getNewBrand().getBrandName()
                        )
                        .itemName(item.getName())
                        .celebName(
                                item.getCeleb() != null
                                        ? item.getCeleb().getCelebNameKr()
                                        : item.getNewCeleb().getCelebName()
                        )
                        .scrapStatus(itemScrapRepository.getItemScrapStatus(item, closetList))
                        .build()
        ).toList();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

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
                List<String> itemImgList = questionItemList.stream().map(questionItem -> itemImgRepository.findMainImg(questionItem.getId()).getItemImgUrl()).toList();

                // 해당 Question의 이미지 리스트 구하기
                List<String> imgList = questionImgRepository.findAllByQuestionId(question.getId()).stream().map(QuestionImg::getImgUrl).toList();

                return QuestionSimpleResDto.builder()
                        .qType("Buy")
                        .id(question.getId())
                        .title(question.getTitle())
                        .content(question.getContent().substring(0, 50) + "...")
                        .imgList(imgList)
                        .itemImgList(itemImgList)
                        .build();
            }).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("Find")){
            Page<QuestionFind> searchQuestionPage =
                    questionRepository.getSearchQuestionFind(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->

                QuestionSimpleResDto.builder()
                        .qType("Find")
                        .id(question.getId())
                        .title(question.getTitle())
                        .content(question.getContent().substring(0, 50) + "...")
                        .celebName(
                                question.getCeleb() != null
                                ?question.getCeleb().getCelebNameKr()
                                :question.getNewCeleb().getCelebName()
                        )
                        .build()
            ).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("How")){
            Page<QuestionHowabout> searchQuestionPage =
                    questionRepository.getSearchQuestionHowabout(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->

                QuestionSimpleResDto.builder()
                        .qType("How")
                        .id(question.getId())
                        .title(question.getTitle())
                        .content(question.getContent().substring(0, 50) + "...")
                        .build()
            ).toList();

            return PaginationResDto.<QuestionSimpleResDto>builder()
                    .page(searchQuestionPage.getNumber())
                    .hasNext(searchQuestionPage.hasNext())
                    .content(content)
                    .build();

        }else if(qType.equals("Recommend")){
            Page<QuestionRecommend> searchQuestionPage =
                    questionRepository.getSearchQuestionRecommend(questionIdList, pageable);

            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->
                QuestionSimpleResDto.builder()
                        .qType("Recommend")
                        .id(question.getId())
                        .title(question.getTitle())
                        .content(question.getContent().substring(0, 50) + "...")
                        .categoryName(questionRecommendCategoryRepository.findOneByQuestionId(question.getId()).getName())
                        .build()
            ).toList();

            // 최근 검색 등록
            postRecentSearch(user, keyword);

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
                UserSearchInfoDto.builder()
                        .id(searchUser.getId())
                        .nickName(searchUser.getNickname())
                        .profileImgUrl(searchUser.getProfileImgUrl())
                        .followStatus(followRepository.getFollowStatus(user, searchUser))
                        .build()
        ).toList();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

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
        Stream<QuestionSimpleResDto> resultStream;

        Stream<QuestionSimpleResDto> streamFind =
                this.getSearchQuestion(user, keyword, "Find", questionPageable).getContent().stream();

        resultStream = streamFind;
        if(resultStream.toList().size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "Find", questionPageable).getContent();
            resultStream = Stream.concat(streamFind, temp.stream());

        }

        if(resultStream.toList().size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "How", questionPageable).getContent();
            resultStream = Stream.concat(streamFind, temp.stream());

        }
        if(resultStream.toList().size() < 4){
            List<QuestionSimpleResDto> temp =
                    this.getSearchQuestion(user, keyword, "Recommend", questionPageable).getContent();
            resultStream = Stream.concat(streamFind, temp.stream());
        }

        List<QuestionSimpleResDto> searchQuestion = resultStream.toList();
        // User 검색
        Pageable userPageable = PageRequest.of(0, userSize);

        List<UserSearchInfoDto> searchUser = this.getSearchUser(user, keyword, userPageable).getContent();

        // 최근 검색 등록
        postRecentSearch(user, keyword);

        return SearchTotalResDto.builder()
                .itemList(searchItem)
                .questionList(searchQuestion)
                .userList(searchUser)
                .build();
    }

    public SearchItemCountResDto getSearchItemCount(String keyword, SearchFilterReqDto dto) {
        // ElasticSearch API Path
        String itemPath = "/search/searchItem";

        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        return SearchItemCountResDto.builder()
                .itemCount(itemRepository.getSearchItemCount(itemIdList, dto))
                .build();
    }

    private void postRecentSearch(User user, String keyword){
        RecentSearch recentSearch = RecentSearch.builder()
                .user(user)
                .searchWord(keyword)
                .build();

        log.info("Post RecentSearch -> User: {}, Keyword: {}", user.getId(), keyword);
        recentSearchRepository.save(recentSearch);

    }

    public List<RecentSearchChipResDto> getRecentSearch(User user) {
        List<RecentSearchChipResDto> result
                = recentSearchRepository.getRecentSearch(user)
                .stream()
                .map(recentSearch -> RecentSearchChipResDto.builder()
                        .keyword(recentSearch.getSearchWord())
                        .build()
                ).toList();

        return result;
    }
}
