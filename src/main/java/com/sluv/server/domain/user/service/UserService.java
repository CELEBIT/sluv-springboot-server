package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebChildResDto;
import com.sluv.server.domain.celeb.repository.InterestedCelebRepository;
import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.dto.CommentSimpleResDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.ItemLike;
import com.sluv.server.domain.item.entity.RecentItem;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.*;
import com.sluv.server.domain.user.dto.*;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.exception.UserNicknameDuplicatedException;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationCountResDto;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final CelebRepository celebRepository;
    private final FollowRepository followRepository;
    private final InterestedCelebRepository interestedCelebRepository;
    private final ItemRepository itemRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final RecentItemRepository recentItemRepository;
    private final ClosetRepository closetRepository;
    private final ItemLikeRepository itemLikeRepository;

    private final RecentQuestionRepository recentQuestionRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final QuestionLikeRepository questionLikeRepository;

    private final JwtProvider jwtProvider;


    public UserDto getUserIdByToken(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
            return UserDto.builder()
                           .id(jwtProvider.getUserId(token))
                            .build();
    }

    /**
     * == user의 관심 Celeb 검색
     * @param user
     */
    public List<InterestedCelebParentResDto> getInterestedCeleb(User user) {
        List<Celeb> interestedCelebs = celebRepository.findInterestedCeleb(user);

        return interestedCelebs.stream()
                .map(celeb -> {
                    List<InterestedCelebChildResDto> subDtoList = null;
                    if(!celeb.getSubCelebList().isEmpty()){
                         subDtoList = celeb.getSubCelebList().stream()
                                .map(subCeleb -> InterestedCelebChildResDto.builder()
                                .id(subCeleb.getId())
                                .celebNameKr(subCeleb.getCelebNameKr())
                                .build()
                                ).toList();
                    }

                    return InterestedCelebParentResDto.builder()
                            .id(celeb.getId())
                            .celebNameKr(celeb.getCelebNameKr())
                            .subCelebList(subDtoList)
                            .build();

                }).toList();

    }

    @Transactional
    public void postUserFollow(User user, Long userId) {
        // target이 될 유저 검색
        User targetUser = userRepository.findById(userId)
                                            .orElseThrow(UserNotFoundException::new);

        // Follow 여부 확인.
        Boolean followStatus = followRepository.getFollowStatus(user, targetUser);

        if(followStatus) {
            followRepository.deleteFollow(user, targetUser);
        }else {
            // Follow 정보 등록.
            followRepository.save(
                    Follow.builder()
                            .follower(user)
                            .followee(targetUser)
                            .build()
            );
        }

    }

    @Transactional
    public void postInterestedCeleb(User user, InterestedCelebPostReqDto dto) {
        // 기존에 있는 해당 유저의 관심셀럽 초기화
        interestedCelebRepository.deleteAllByUserId(user.getId());

        // 초기화 상태에서 다시 추가.
        List<InterestedCeleb> interestedCelebList = dto.getCelebIdList().stream()
                .map(celeb ->
                        InterestedCeleb.builder()
                                .user(user)
                                .celeb(celebRepository.findById(celeb).orElseThrow(CelebNotFoundException::new))
                                .build()
                ).toList();

        interestedCelebRepository.saveAll(interestedCelebList);
    }

    @Transactional
    public void postUserProfile(User user, UserProfileReqDto dto) {
        User currentUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);

        // 닉네임 중복 검사
        Boolean nicknameExistsStatus = userRepository.existsByNickname(dto.getNickName());
        if (nicknameExistsStatus){
            throw new UserNicknameDuplicatedException();
        }

        currentUser.changeNickname(dto.getNickName());
        currentUser.changeProfileImgUrl(dto.getImgUrl());

        if(currentUser.getUserStatus().equals(UserStatus.PENDING_PROFILE)){
            currentUser.changeUserStatus(UserStatus.PENDING_CELEB);
        }
    }

    public UserMypageResDto getUserMypage(User user, Long userId) {
        UserMypageResDto.UserMypageResDtoBuilder result = UserMypageResDto.builder();
        User targetUser;
        if(user.getId().equals(userId)){ // 특정 유저와 현재 유저가 같을 때
            targetUser = user;

            Long questionNum = questionRepository.countByUserId(targetUser.getId());
            Long commentNum = commentRepository.countByUserId(targetUser.getId());

            List<String> imgList = itemRepository.getRecentTop2Item(targetUser)
                    .stream()
                    .map(item -> itemImgRepository.findMainImg(item.getId()).getItemImgUrl()).toList();

            result
                .itemCount(itemRepository.countByUserId(targetUser.getId()))
                .imgList(imgList)
                .communityCount(questionNum + commentNum);

        }else{ // 특정 유저와 현재 유저가 다를 때
            targetUser = userRepository.findById(userId)
                                            .orElseThrow(UserNotFoundException::new);
        }



        List<InterestedCelebResDto> interestedCelebList = interestedCelebRepository.findAllByUserId(targetUser.getId())
                .stream().map(interestedCeleb -> InterestedCelebResDto.builder()
                        .id(interestedCeleb.getCeleb().getId())
                        .celebNameKr(interestedCeleb.getCeleb().getCelebNameKr())
                        .celebCategory(
                                interestedCeleb.getCeleb().getCelebCategory().getParent() != null
                                ? interestedCeleb.getCeleb().getCelebCategory().getParent().getName()
                                : interestedCeleb.getCeleb().getCelebCategory().getName()
                        )
                        .build()
                ).toList();


        return result
                .userInfo(
                        UserInfoDto.builder()
                            .id(targetUser.getId())
                            .nickName(targetUser.getNickname())
                            .profileImgUrl(targetUser.getProfileImgUrl())
                            .build()
                )
                .followerCount(followRepository.getFollowerCount(targetUser))
                .followingCount(followRepository.getFollowingCount(targetUser))
                .interestedCelebList(interestedCelebList)
                .build();
    }

    public PaginationResDto<ItemSimpleResDto> getUserItem(User user, Long userId, Pageable pageable) {
        Page<Item> itemPage = itemRepository.getUserAllItem(userId, pageable);

        List<ItemSimpleResDto> content = itemPage.stream().map(item -> {
            List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
            return getItemSimpleResDto(item, closetList);
        }).toList();

        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(itemPage.getNumber())
                .hasNext(itemPage.hasNext())
                .content(content)
                .build();
    }

    private ItemSimpleResDto getItemSimpleResDto(Item item, List<Closet> closetList) {
        return ItemSimpleResDto.builder()
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
                                ? item.getCeleb().getParent() != null
                                ? item.getCeleb().getParent().getCelebNameKr() + " " + item.getCeleb().getCelebNameKr()
                                : item.getCeleb().getCelebNameKr()
                                : item.getNewCeleb().getCelebName()
                )
                .scrapStatus(itemScrapRepository.getItemScrapStatus(item, closetList))
                .build();
    }

    public PaginationResDto<ClosetResDto> getUserCloset(User user, Long userId, Pageable pageable) {
        Page<Closet> closetPage;

        // User 일치 여부에 따라 조회하는 Public Closet만 조회할지 결정
        if(user.getId().equals(userId)){
            closetPage = closetRepository.getUserAllCloset(userId, pageable);
        }else {
            closetPage = closetRepository.getUserAllPublicCloset(userId, pageable);
        }

        List<ClosetResDto> content = closetPage.stream().map(closet ->
                ClosetResDto.builder()
                        .name(closet.getName())
                        .coverImgUrl(closet.getCoverImgUrl())
                        .closetStatus(closet.getClosetStatus())
                        .color(closet.getColor())
                        .itemNum(itemScrapRepository.countByClosetId(closet.getId()))
                        .build()
        ).toList();

        return PaginationResDto.<ClosetResDto>builder()
                .page(closetPage.getNumber())
                .hasNext(closetPage.hasNext())
                .content(content)
                .build();
    }

    public PaginationCountResDto<ItemSimpleResDto> getUserRecentItem(User user, Pageable pageable) {

        Page<RecentItem> recentItemPage = recentItemRepository.getUserAllRecentItem(user, pageable);

        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        List<ItemSimpleResDto> content = recentItemPage
                .stream()
                .map(recentItem -> getItemSimpleResDto(recentItem.getItem(), closetList))
                .toList();

        return new PaginationCountResDto<>(recentItemPage.hasNext(), recentItemPage.getNumber(), content, recentItemPage.getTotalElements());

    }

    public PaginationCountResDto<QuestionSimpleResDto> getUserRecentQuestion(User user, Pageable pageable) {

        Page<RecentQuestion> recentQuestionPage =  recentQuestionRepository.getUserAllRecentQuestion(user, pageable);

        List<QuestionSimpleResDto> content = recentQuestionPage.stream().map(recentQuestion -> {
            Question question = questionRepository.findById(recentQuestion.getQuestion().getId())
                    .orElseThrow(QuestionNotFoundException::new);

            QuestionSimpleResDto.QuestionSimpleResDtoBuilder builder = QuestionSimpleResDto.builder()
                    .qType(recentQuestion.getQType())
                    .id(question.getId())
                    .title(question.getTitle())
                    .content(question.getContent());

            if (recentQuestion.getQType().equals("Buy")) {
                // 이미지 Dto 생성
                List<QuestionImgSimpleResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(this::convertQuestionImgToQuestionImgSimpleResDto).toList();
                // 아이템 이미지 Dto 생성
                List<QuestionImgSimpleResDto> itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(this::convertQuestionItemToQuestionImgSimpleResDto).toList();
                builder
                    .imgList(imgList)
                    .itemImgList(itemImgList);

            } else if (recentQuestion.getQType().equals("How")) {

            } else if (recentQuestion.getQType().equals("Recommend")) {
                // Question 카테고리
                List<String> categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionRecommendCategory::getName).toList();
                builder
                    .categoryName(categoryList);
            } else if (recentQuestion.getQType().equals("Find")) {
                QuestionFind questionFind = (QuestionFind) question;
                builder
                        .celebName(
                                questionFind.getCeleb() != null
                                ?questionFind.getCeleb().getParent() != null
                                        ?questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                                        :questionFind.getCeleb().getCelebNameKr()
                                :questionFind.getNewCeleb().getCelebName()
                        );
            } else {
                throw new QuestionTypeNotFoundException();
            }

            return builder.build();


        }).toList();

        return new PaginationCountResDto<>(
                recentQuestionPage.hasNext(),
                recentQuestionPage.getNumber(),
                content,
                recentQuestionPage.getTotalElements()
        );
    }

    public PaginationCountResDto<ItemSimpleResDto> getUserLikeItem(User user, Pageable pageable) {
        Page<Item> itemPage = itemRepository.getAllByUserLikeItem(user, pageable);

        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        List<ItemSimpleResDto> content = itemPage.stream().map(item -> getItemSimpleResDto(item, closetList)).toList();

        return new PaginationCountResDto<>(itemPage.hasNext(), itemPage.getNumber(), content, itemPage.getTotalElements());
    }

    public PaginationResDto<UserSearchInfoDto> getUserFollower(User user, Long userId, Pageable pageable) {
        // Follower 들 조회
        Page<User> followerPage = userRepository.getAllFollower(userId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content = followerPage.stream().map(follower -> UserSearchInfoDto.builder()
                .id(follower.getId())
                .nickName(follower.getNickname())
                .profileImgUrl(follower.getProfileImgUrl())
                .followStatus(followRepository.getFollowStatus(user, follower))
                .build()
        ).toList();

        return PaginationResDto.<UserSearchInfoDto>builder()
                .page(followerPage.getNumber())
                .hasNext(followerPage.hasNext())
                .content(content)
                .build();
    }

    public PaginationResDto<UserSearchInfoDto> getUserFollowing(User user, Long userId, Pageable pageable) {
        // Following 들 조회
        Page<User> followerPage = userRepository.getAllFollowing(userId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content = followerPage.stream().map(follower -> UserSearchInfoDto.builder()
                .id(follower.getId())
                .nickName(follower.getNickname())
                .profileImgUrl(follower.getProfileImgUrl())
                .followStatus(followRepository.getFollowStatus(user, follower))
                .build()
        ).toList();

        return PaginationResDto.<UserSearchInfoDto>builder()
                .page(followerPage.getNumber())
                .hasNext(followerPage.hasNext())
                .content(content)
                .build();
    }

    public void patchUserProfileImg(User user, UserProfileImgReqDto dto) {
        log.info("User Profile Img Change. User: {}", user.getId());
        log.info("Change URL: {}", dto.getImgUrl());
        user.changeProfileImgUrl(dto.getImgUrl());
        userRepository.save(user);
    }

    public void deleteUserProfileImg(User user) {
        log.info("User Profile Img Delete. User: {}", user.getId());
        user.changeProfileImgUrl(null);
        userRepository.save(user);
    }

    /**
     * 유저가 좋아요한 Question 게시글 조회
     */

    public PaginationCountResDto<QuestionSimpleResDto> getUserLikeQuestion(User user, Pageable pageable) {
        Page<Question> questionPage = questionRepository.getUserLikeQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream().map(this::dtoBuildByQuestionType).toList();


        return new PaginationCountResDto<>(
                questionPage.hasNext(),
                questionPage.getNumber(),
                content,
                questionPage.getTotalElements()
        );
    }

    private QuestionSimpleResDto dtoBuildByQuestionType(Question question) {
        String qType;
        if(question instanceof QuestionBuy){
            qType = "Buy";
        }else if(question instanceof QuestionFind){
            qType = "Find";
        }else if(question instanceof QuestionHowabout){
            qType = "How";
        }else if(question instanceof QuestionRecommend){
            qType = "Recommend";
        }else{
            throw new QuestionTypeNotFoundException();
        }
        return getQuestionSimpleResDto(qType, question);
    }

    private QuestionSimpleResDto getQuestionSimpleResDto(String qType, Question question) {
        QuestionSimpleResDto.QuestionSimpleResDtoBuilder builder = QuestionSimpleResDto.builder()
                .qType(qType)
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent());

        if(qType.equals("Buy")){
            // 이미지 DTO 생성
            List<QuestionImgSimpleResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(this::convertQuestionImgToQuestionImgSimpleResDto).toList();
            // 아이템 이미지 DTO 생성
            List<QuestionImgSimpleResDto> itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(this::convertQuestionItemToQuestionImgSimpleResDto).toList();

            builder
                .imgList(imgList)
                .itemImgList(itemImgList);

        } else if (qType.equals("Find")) {
            QuestionFind questionFind = (QuestionFind) question;
            builder
                    .celebName(questionFind.getCeleb() != null
                    ?questionFind.getCeleb().getParent() != null
                            ? questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                            : questionFind.getCeleb().getCelebNameKr()
                    : questionFind.getNewCeleb().getCelebName()
                    );
        } else if (qType.equals("How")) {

        } else if (qType.equals("Recommend")) {
            List<String> categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionRecommendCategory::getName).toList();
            builder
                        .categoryName(categoryList);
        }else{
            throw new QuestionTypeNotFoundException();
        }

        return builder.build();
    }

    private QuestionImgSimpleResDto convertQuestionImgToQuestionImgSimpleResDto(QuestionImg questionImg){
        return QuestionImgSimpleResDto.builder()
                .imgUrl(questionImg.getImgUrl())
                .sortOrder((long)questionImg.getSortOrder())
                .build();
    }

    private QuestionImgSimpleResDto convertQuestionItemToQuestionImgSimpleResDto(QuestionItem questionItem){
        ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
        return QuestionImgSimpleResDto.builder()
                .imgUrl(mainImg.getItemImgUrl())
                .sortOrder((long) mainImg.getSortOrder())
                .build();
    }

    /**
     * 유저가 좋아요한 댓글 목록 조회
     */

    public PaginationCountResDto<CommentSimpleResDto> getUserLikeComment(User user, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getAllUserLikeComment(user, pageable);

        List<CommentSimpleResDto> content = commentPage.stream().map(this::convertCommentToCommentSimpleResDto).toList();

        return new PaginationCountResDto<>(
                commentPage.hasNext(),
                commentPage.getNumber(),
                content,
                commentPage.getTotalElements()
        );
    }

    private CommentSimpleResDto convertCommentToCommentSimpleResDto(Comment comment){

        return CommentSimpleResDto.builder()
                .id(comment.getId())
                .questionTitle(comment.getQuestion().getTitle())
                .content(comment.getContent())
                .build();
    }

    /**
     * 현재 유저가 업로드한 아이템 조회
     */

    public PaginationCountResDto<ItemSimpleResDto> getUserUploadItem(User user, Pageable pageable) {
        // 현재 유저가 업로드한 아이템 조회
        Page<Item> itemPage = itemRepository.getUserAllItem(user.getId(), pageable);
        // 현재 유저의 옷장 검색
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
        // content 제작
        List<ItemSimpleResDto> content = itemPage.stream().map(item ->
                getItemSimpleResDto(item, closetList)
        ).toList();

        return new PaginationCountResDto<>(
                itemPage.hasNext(),
                itemPage.getNumber(),
                content,
                itemPage.getTotalElements()
        );
    }

    public PaginationCountResDto<QuestionSimpleResDto> getUserUploadQuestion(User user, Pageable pageable) {
        Page<Question> questionPage = questionRepository.getUserAllQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream().map(this::dtoBuildByQuestionType).toList();

        return new PaginationCountResDto<>(
                questionPage.hasNext(),
                questionPage.getNumber(),
                content,
                questionPage.getTotalElements()
        );
    }
}
