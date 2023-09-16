package com.sluv.server.domain.user.service;

import com.sluv.server.domain.auth.exception.UserBlockedException;
import com.sluv.server.domain.celeb.dto.*;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
import com.sluv.server.domain.celeb.repository.CelebRepository;
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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


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
    private final CelebCategoryRepository celebCategoryRepository;
    private final ItemLikeRepository itemLikeRepository;

    private final RecentQuestionRepository recentQuestionRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final QuestionLikeRepository questionLikeRepository;

    private final JwtProvider jwtProvider;


    public UserIdDto getUserIdByToken(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        return UserIdDto.of(jwtProvider.getUserId(token));
    }

    /**
     * User가 선택한 관심 셀럽을 검색
     * 관심 샐럼의 상위 카테고리를 기준으로 묶어서 Response
     */
    public List<InterestedCelebCategoryResDto> getInterestedCelebByCategory(User user) {
        List<Celeb> interestedCelebList = celebRepository.findInterestedCeleb(user);

        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                .parallel()
                // 카테고리별 InterestedCelebCategoryResDto 생성
                .map(category ->  {
                        List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                        return InterestedCelebCategoryResDto.of(category,
                                convertInterestedCelebParentResDto(categoryFilterCeleb));
                    }
                ).toList();
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
                    Follow.toEntity(user, targetUser)
            );
        }

    }

    /**
     * 관심셀럽 목록에서 category와 일치하는 Celeb을 분류
     */
    private List<Celeb> getCategoryFilterCeleb(List<Celeb> celebList, CelebCategory category) {
        return celebList
                .stream()
                .filter(celeb ->
                        // interestedCeleb의 상위 카테고리 id와 카테고리별 묶을 카테고리의 아이디가 일치하는 것만 filtering
                        Objects.equals(celeb.getCelebCategory().getParent() != null
                                        ? celeb.getCelebCategory().getParent().getId()
                                        : celeb.getCelebCategory().getId()
                                , category.getId())
                ).toList();
    }

    /**
     * 관심셀럽 목록에서 category와 일치하는 Celeb을 분류
     */
    private List<InterestedCelebParentResDto> convertInterestedCelebParentResDto(List<Celeb> celebList) {
        return celebList
                .stream()
                .map(InterestedCelebParentResDto::of).toList();
    }

    @Transactional
    public void postInterestedCeleb(User user, InterestedCelebPostReqDto dto) {
        // 기존에 있는 해당 유저의 관심셀럽 초기화
        interestedCelebRepository.deleteAllByUserId(user.getId());

        // 초기화 상태에서 다시 추가.
        List<InterestedCeleb> interestedCelebList = dto.getCelebIdList().stream()
                .map(celeb ->
                        InterestedCeleb.toEntity(
                                user,
                                celebRepository.findById(celeb).orElseThrow(CelebNotFoundException::new)
                        )
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
        User targetUser;
        Long itemCount = null;
        List<String> imgList = null;
        Long communityCount = null;

        if(userId == null){ // 현재 유저일때
            targetUser = user;

            Long questionNum = questionRepository.countByUserId(targetUser.getId());
            Long commentNum = commentRepository.countByUserId(targetUser.getId());

            communityCount = questionNum + commentNum;

            imgList = itemRepository.getRecentTop2Item(targetUser)
                                    .stream()
                                    .map(item -> itemImgRepository.findMainImg(item.getId()).getItemImgUrl())
                                    .toList();

            itemCount = itemRepository.countByUserId(targetUser.getId());

        }else{ // 특정 유저일때
            targetUser = userRepository.findById(userId)
                                            .orElseThrow(UserNotFoundException::new);
        }

        Boolean followStatus = followRepository.getFollowStatus(user, targetUser);
        Long followerCount = followRepository.getFollowerCount(targetUser);
        Long followingCount = followRepository.getFollowingCount(targetUser);

        return UserMypageResDto.of(targetUser,
                followStatus,
                followerCount,
                followingCount,
                itemCount,
                imgList,
                communityCount
                );
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
        return ItemSimpleResDto.of(
                    item,
                    itemImgRepository.findMainImg(item.getId()),
                    itemScrapRepository.getItemScrapStatus(item, closetList)
                );
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
                ClosetResDto.of(
                        closet,
                        itemScrapRepository.countByClosetId(closet.getId())
                        )
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
            List<QuestionImgSimpleResDto> imgList = null;
            List<QuestionImgSimpleResDto> itemImgList = null;
            List<String> categoryList = null;
            String celebName = null;


            if (recentQuestion.getQType().equals("Buy")) {
                // 이미지 Dto 생성
                imgList = questionImgRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionImgSimpleResDto::of).toList();
                // 아이템 이미지 Dto 생성
                itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(questionItem -> {
                                ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                                return QuestionImgSimpleResDto.of(mainImg);
                        }).toList();

            } else if (recentQuestion.getQType().equals("How")) {

            } else if (recentQuestion.getQType().equals("Recommend")) {
                // Question 카테고리
                categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionRecommendCategory::getName).toList();
            } else if (recentQuestion.getQType().equals("Find")) {
                QuestionFind questionFind = (QuestionFind) question;
                celebName = questionFind.getCeleb() != null
                                ?questionFind.getCeleb().getParent() != null
                                        ?questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                                        :questionFind.getCeleb().getCelebNameKr()
                                :questionFind.getNewCeleb().getCelebName();
            } else {
                throw new QuestionTypeNotFoundException();
            }

            // 작성자 InfoDto
            UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

            // Question 좋아요 수
            Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

            // Question 댓글 수
            Long commentNum = commentRepository.countByQuestionId(question.getId());

            return QuestionSimpleResDto.of(recentQuestion.getQType(), userInfoDto, likeNum, commentNum,
                    question, celebName, imgList, itemImgList, categoryList);



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
        List<UserSearchInfoDto> content = followerPage.stream().map(follower ->
                UserSearchInfoDto.of(
                    follower,
                    followRepository.getFollowStatus(user, follower)
                )
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
        List<UserSearchInfoDto> content = followerPage.stream().map(follower ->
                UserSearchInfoDto.of(
                        follower,
                        followRepository.getFollowStatus(user, follower)
                )
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
        String qType = null;
        List<QuestionImgSimpleResDto> imgList = null;
        List<QuestionImgSimpleResDto> itemImgList = null;
        String celebName = null;
        List<String> categoryList = null;

        if(question instanceof QuestionBuy){ // 1. question이 QuestionBuy 일 경우
            // 이미지 DTO 생성
            List<QuestionImgSimpleResDto> imgSimpleList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionImgSimpleResDto::of).toList();
            // 아이템 이미지 DTO 생성
            List<QuestionImgSimpleResDto> itemImgSimpleList = questionItemRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionItem -> {
                        ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                        return QuestionImgSimpleResDto.of(mainImg);
                    }).toList();

            qType = "Buy";
            imgList = imgSimpleList;
            itemImgList = itemImgSimpleList;

        } else if (question instanceof QuestionFind questionFind) { // 2. question이 QuestionFind 일 경우
            qType = "Find";
            celebName = questionFind.getCeleb() != null
                        ?questionFind.getCeleb().getParent() != null
                                ? questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                                : questionFind.getCeleb().getCelebNameKr()
                        : questionFind.getNewCeleb().getCelebName();

        } else if (question instanceof QuestionHowabout) { // 3. question이 QuestionHowabout 일 경우
            qType = "How";

        } else if (question instanceof QuestionRecommend) { // 4. question이 QuestionRecommend 일 경우
            List<String> categoryNameList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionRecommendCategory::getName).toList();
            qType = "Recommend";
            categoryList = categoryNameList;

        }else{
            throw new QuestionTypeNotFoundException();
        }
        // 작성자 InfoDto
        UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

        // Question 좋아요 수
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

        // Question 댓글 수
        Long commentNum = commentRepository.countByQuestionId(question.getId());

        return QuestionSimpleResDto.of(qType, userInfoDto, likeNum, commentNum,
                question, celebName, imgList, itemImgList, categoryList);
    }

    /**
     * 유저가 좋아요한 댓글 목록 조회
     */

    public PaginationCountResDto<CommentSimpleResDto> getUserLikeComment(User user, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getUserAllLikeComment(user, pageable);

        List<CommentSimpleResDto> content = commentPage.stream().map(CommentSimpleResDto::of).toList();

        return new PaginationCountResDto<>(
                commentPage.hasNext(),
                commentPage.getNumber(),
                content,
                commentPage.getTotalElements()
        );
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

    /**
     * 현재 유저가 업로그한 Question 조회
     */

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

    /**
     * 현재 유저가 업로그한 Comment 조회
     */
    public PaginationCountResDto<CommentSimpleResDto> getUserUploadComment(User user, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getUserAllComment(user, pageable);

        List<CommentSimpleResDto> content = commentPage.stream().map(CommentSimpleResDto::of).toList();

        return new PaginationCountResDto<>(
                commentPage.hasNext(),
                commentPage.getNumber(),
                content,
                commentPage.getTotalElements()
        );
    }

    public List<UserSearchInfoDto> getHotSluver(User user, Long celebId) {
        List<User> userList = userRepository.getHotSluver(user, celebId);

        return userList.stream().map(_user ->
                UserSearchInfoDto.of(
                        _user,
                        followRepository.getFollowStatus(user, _user)
                )
        ).toList();


    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회
     * CelebCategory를 기준으로 그룹핑
     * 카테고리를 기준으로 조회
     */

    public List<InterestedCelebCategoryResDto> getTargetUserInterestedCelebByCategory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Celeb> interestedCelebList = celebRepository.findInterestedCeleb(user);

        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();
        categoryList.sort(Comparator.comparing(CelebCategory::getName));

        return categoryList.stream()
                // 카테고리별 InterestedCelebCategoryResDto 생성
                .map(category ->  {
                            List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                            return InterestedCelebCategoryResDto.of(category,
                                    convertInterestedCelebParentResDto(categoryFilterCeleb));
                        }
                ).toList();
    }

    /**
     * 가수 -> 배우 -> 방송인 -> 스포츠인 -> 인플루언서 순서로 변
     */

    private void changeCategoryOrder(List<CelebCategory> categoryList) {
        categoryList.sort(Comparator.comparing(CelebCategory::getName));

        CelebCategory tempCategory = categoryList.get(1);
        categoryList.set(1, categoryList.get(2));
        categoryList.set(2, tempCategory);
    }

    public void checkUserStatue(User user) {

        if (user.getUserStatus().equals(UserStatus.BLOCKED)){
            throw new UserBlockedException();
        }
    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회
     * CelebCategory를 기준으로 그룹핑
     * 유저가 등록한 순서대로 조회
     */

    public List<InterestedCelebParentResDto> getTargetUserInterestedCelebByPostTime(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return celebRepository.findInterestedCeleb(user)
                .stream()
                .map(InterestedCelebParentResDto::of).toList();
    }

    /**
     * User가 선택한 관심 셀럽을 검색
     * 관심 셀럽 선택순서를 기준으로 조회
     */
    public List<InterestedCelebParentResDto> getInterestedCelebByPostTime(User user) {
        return celebRepository.findInterestedCeleb(user)
                .stream()
                .map(InterestedCelebParentResDto::of).toList();
    }
}
