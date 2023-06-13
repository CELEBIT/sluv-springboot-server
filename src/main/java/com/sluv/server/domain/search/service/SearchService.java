package com.sluv.server.domain.search.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
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
import com.sluv.server.domain.search.utils.ElasticSearchConnectUtil;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    /**
     * Item 검색 with ElasticSearch
     */
    public PaginationResDto<ItemSimpleResDto> getSearchItem(User user, String keyword, Pageable pageable) {
        // ElasticSearch API Path
        String itemPath = "/search/searchItem";

        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        // 조건에 맞는 Item Page 조회
        Page<Item> searchItemPage = itemRepository.getSearchItem(itemIdList, pageable);

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


        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(searchItemPage.getNumber())
                .hasNext(searchItemPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * Question 검색 with ElasticSearch
     */
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


        return PaginationResDto.<UserSearchInfoDto>builder()
                .page(searchUserPage.getNumber())
                .hasNext(searchUserPage.hasNext())
                .content(content)
                .build();
    }
}
