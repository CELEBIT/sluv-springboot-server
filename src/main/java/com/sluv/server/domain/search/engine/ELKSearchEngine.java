//package com.sluv.server.domain.search.service;
//
//import com.sluv.server.domain.closet.entity.Closet;
//import com.sluv.server.domain.closet.repository.ClosetRepository;
//import com.sluv.server.domain.comment.repository.CommentRepository;
//import com.sluv.server.domain.item.dto.ItemSimpleResDto;
//import com.sluv.server.domain.item.entity.Item;
//import com.sluv.server.domain.item.entity.ItemImg;
//import com.sluv.server.domain.item.repository.ItemImgRepository;
//import com.sluv.server.domain.item.repository.ItemRepository;
//import com.sluv.server.domain.item.repository.ItemScrapRepository;
//import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
//import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
//import com.sluv.server.domain.question.entity.QuestionBuy;
//import com.sluv.server.domain.question.entity.QuestionFind;
//import com.sluv.server.domain.question.entity.QuestionHowabout;
//import com.sluv.server.domain.question.entity.QuestionItem;
//import com.sluv.server.domain.question.entity.QuestionRecommend;
//import com.sluv.server.domain.question.entity.QuestionRecommendCategory;
//import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
//import com.sluv.server.domain.question.repository.QuestionImgRepository;
//import com.sluv.server.domain.question.repository.QuestionItemRepository;
//import com.sluv.server.domain.question.repository.QuestionLikeRepository;
//import com.sluv.server.domain.question.repository.QuestionRecommendCategoryRepository;
//import com.sluv.server.domain.question.repository.QuestionRepository;
//import com.sluv.server.domain.search.dto.SearchFilterReqDto;
//import com.sluv.server.domain.search.dto.SearchItemCountResDto;
//import com.sluv.server.domain.search.utils.ElasticSearchConnectUtil;
//import com.sluv.server.domain.user.dto.UserSearchInfoDto;
//import com.sluv.server.domain.user.entity.User;
//import com.sluv.server.domain.user.repository.FollowRepository;
//import com.sluv.server.domain.user.repository.UserRepository;
//import com.sluv.server.global.common.response.PaginationResDto;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//public class ELKSearchEngineService implements SearchEngineService {
//
//    private final ItemRepository itemRepository;
//    private final ItemImgRepository itemImgRepository;
//    private final ItemScrapRepository itemScrapRepository;
//    private final QuestionRepository questionRepository;
//    private final QuestionItemRepository questionItemRepository;
//    private final QuestionImgRepository questionImgRepository;
//    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
//    private final UserRepository userRepository;
//    private final FollowRepository followRepository;
//    private final QuestionLikeRepository questionLikeRepository;
//    private final CommentRepository commentRepository;
//
//    private final ClosetRepository closetRepository;
//    private final ElasticSearchConnectUtil elasticSearchConnectUtil;
//    private final SearchService searchService;
//
//
//    /**
//     * Item 검색 with ElasticSearch
//     */
//    @Async(value = "asyncThreadPoolExecutor")
//    public CompletableFuture<PaginationResDto<ItemSimpleResDto>> getSearchItem(User user, String keyword,
//                                                                               SearchFilterReqDto dto,
//                                                                               Pageable pageable) {
//        // ElasticSearch API Path
//        String itemPath = "/search/searchItem";
//
//        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
//        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);
//
//        // 조건에 맞는 Item Page 조회
//        Page<Item> searchItemPage = itemRepository.getSearchItem(itemIdList, dto, pageable);
//
//        // 현재 접속한 User의 옷장 목록 조회
//        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
//
//        // Cotent 조립
//        List<ItemSimpleResDto> content = searchItemPage.stream().map(item ->
//                ItemSimpleResDto.of(
//                        item,
//                        itemImgRepository.findMainImg(item.getId()),
//                        itemScrapRepository.getItemScrapStatus(item, closetList)
//                )
//        ).toList();
//
//        // 최근 검색 등록
//        searchService.postRecentSearch(user, keyword);
//
//        // 서치 데이터 등록
//        searchService.postSearchData(keyword);
//
//        return CompletableFuture.completedFuture(PaginationResDto.of(searchItemPage, content));
//    }
//
//    /**
//     * Question 검색 with ElasticSearch
//     */
//    @Async(value = "asyncThreadPoolExecutor")
//    public CompletableFuture<PaginationResDto<QuestionSimpleResDto>> getSearchQuestion(User user, String keyword,
//                                                                                       String qType,
//                                                                                       Pageable pageable) {
//        // ElasticSearch API Path
//        String itemPath = "/search/searchQuestion";
//
//        // ElasticSearch 에서 Keyword에 해당하는 Question의 Id 조회
//        List<Long> questionIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);
//
//        // 조건에 맞는 Item Page 조회
//        if (qType.equals("Buy")) {
//            Page<QuestionBuy> searchQuestionPage =
//                    questionRepository.getSearchQuestionBuy(questionIdList, pageable);
//
//            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->
//            {
//                // 해당 Question의 item 이미지 리스트 구하기
//                List<QuestionItem> questionItemList = questionItemRepository.findAllByQuestionId(question.getId());
//                List<QuestionImgSimpleResDto> itemImgList = questionItemList.stream()
//                        .map(questionItem -> {
//                            ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
//                            return QuestionImgSimpleResDto.of(mainImg);
//                        }).toList();
//
//                // 해당 Question의 이미지 리스트 구하기
//                List<QuestionImgSimpleResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
//                        .stream()
//                        .map(QuestionImgSimpleResDto::of).toList();
//
//                // Question 좋아요 수
//                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
//
//                // Question 댓글 수
//                Long commentNum = commentRepository.countByQuestionId(question.getId());
//
//                return QuestionSimpleResDto.of(question, likeNum, commentNum, imgList, itemImgList, null);
//            }).toList();
//
//            return CompletableFuture.completedFuture(PaginationResDto.of(searchQuestionPage, content));
//
//        } else if (qType.equals("Find")) {
//            Page<QuestionFind> searchQuestionPage =
//                    questionRepository.getSearchQuestionFind(questionIdList, pageable);
//
//            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question -> {
//
//                // Question 좋아요 수
//                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
//
//                // Question 댓글 수
//                Long commentNum = commentRepository.countByQuestionId(question.getId());
//
//                return QuestionSimpleResDto.of(question, likeNum, commentNum, null, null, null);
//
//            }).toList();
//
//            return CompletableFuture.completedFuture(PaginationResDto.of(searchQuestionPage, content));
//
//        } else if (qType.equals("How")) {
//            Page<QuestionHowabout> searchQuestionPage =
//                    questionRepository.getSearchQuestionHowabout(questionIdList, pageable);
//
//            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question -> {
//
//                // Question 좋아요 수
//                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
//
//                // Question 댓글 수
//                Long commentNum = commentRepository.countByQuestionId(question.getId());
//
//                return QuestionSimpleResDto.of(question, likeNum, commentNum, null, null, null);
//            }).toList();
//
//            return CompletableFuture.completedFuture(PaginationResDto.of(searchQuestionPage, content));
//
//        } else if (qType.equals("Recommend")) {
//            Page<QuestionRecommend> searchQuestionPage =
//                    questionRepository.getSearchQuestionRecommend(questionIdList, pageable);
//
//            List<QuestionSimpleResDto> content = searchQuestionPage.stream().map(question ->
//            {
//                List<String> categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
//                        .stream()
//                        .map(QuestionRecommendCategory::getName).toList();
//
//                // Question 좋아요 수
//                Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
//
//                // Question 댓글 수
//                Long commentNum = commentRepository.countByQuestionId(question.getId());
//
//                return QuestionSimpleResDto.of(question, likeNum, commentNum, null, null, categoryList);
//            }).toList();
//
//            // 최근 검색 등록
//            searchService.postRecentSearch(user, keyword);
//
//            // 서치 데이터 등록
//            searchService.postSearchData(keyword);
//
//            return CompletableFuture.completedFuture(PaginationResDto.of(searchQuestionPage, content));
//        }
//
//        throw new QuestionTypeNotFoundException();
//    }
//
//    /**
//     * User 검색 with ElasticSearch
//     */
//    @Async(value = "asyncThreadPoolExecutor")
//    public CompletableFuture<PaginationResDto<UserSearchInfoDto>> getSearchUser(User user, String keyword,
//                                                                                Pageable pageable) {
//        // ElasticSearch API Path
//        String itemPath = "/search/searchUser";
//
//        // ElasticSearch 에서 Keyword에 해당하는 User의 Id 조회
//        List<Long> userIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);
//
//        // 조건에 맞는 User Page 조회
//        Page<User> searchUserPage = userRepository.getSearchUser(userIdList, pageable);
//
//        // Cotent 조립
//        List<UserSearchInfoDto> content = searchUserPage.stream().map(searchUser ->
//                UserSearchInfoDto.of(
//                        searchUser,
//                        followRepository.getFollowStatus(user, searchUser.getId())
//                )
//        ).toList();
//
//        // 최근 검색 등록
//        searchService.postRecentSearch(user, keyword);
//
//        // 서치 데이터 등록
//        searchService.postSearchData(keyword);
//
//        return CompletableFuture.completedFuture(PaginationResDto.of(searchUserPage, content));
//    }
//
//    @Transactional(readOnly = true)
//    public SearchItemCountResDto getSearchItemCount(String keyword, SearchFilterReqDto dto) {
//        // ElasticSearch API Path
//        String itemPath = "/search/searchItem";
//
//        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
//        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);
//
//        return SearchItemCountResDto.of(
//                itemRepository.getSearchItemCount(itemIdList, dto)
//        );
//    }
//
//}
