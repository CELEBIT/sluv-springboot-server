package com.sluv.api.user.service;

import com.sluv.api.closet.dto.response.ClosetResponse;
import com.sluv.api.comment.dto.reponse.CommentSimpleResponse;
import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.item.helper.ItemHelper;
import com.sluv.api.item.service.ItemCacheService;
import com.sluv.api.item.service.TempItemService;
import com.sluv.api.question.mapper.QuestionDtoMapper;
import com.sluv.api.user.dto.*;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.exception.UserNicknameDuplicatedException;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.domain.user.service.UserWithdrawDomainService;
import com.sluv.infra.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDomainService userDomainService;
    private final FollowDomainService followDomainService;
    private final ItemDomainService itemDomainService;
    private final QuestionDomainService questionDomainService;
    private final CommentDomainService commentDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final ItemScrapDomainService itemScrapDomainService;
    private final ClosetDomainService closetDomainService;
    private final UserWithdrawDomainService userWithdrawDomainService;

    private final ItemHelper itemHelper;
    private final QuestionDtoMapper questionDtoMapper;
    private final ItemCacheService itemCacheService;
    private final WebHookService webHookService;
    private final UserWithdrawDataService userWithdrawDataService;
    private final TempItemService tempItemService;

    @Transactional
    public void postUserProfile(Long userId, UserProfileReqDto dto) {
        User currentUser = userDomainService.findById(userId);
        itemCacheService.deleteAllItemCacheByUserId(userId);
        // 닉네임 중복 검사
        Boolean nicknameExistsStatus = userDomainService.existsByNickname(dto.getNickName());
        if (nicknameExistsStatus) {
            throw new UserNicknameDuplicatedException();
        }

        currentUser.changeNickname(dto.getNickName());
        currentUser.changeProfileImgUrl(dto.getImgUrl());

        if (currentUser.getUserStatus().equals(UserStatus.PENDING_PROFILE)) {
            currentUser.changeUserStatus(UserStatus.PENDING_CELEB);
        }
    }

    @Transactional(readOnly = true)
    public UserMypageResDto getUserMypage(Long userId, Long targetId) {
        User targetUser;
        Long itemCount = null;
        List<String> imgList = null;
        Long communityCount = null;

        User user = userDomainService.findById(userId);

        if (targetId == null) { // 현재 유저일때
            targetUser = user;

            Long questionNum = questionDomainService.countByUserIdAndQuestionStatus(targetUser.getId(),
                    QuestionStatus.ACTIVE);
            Long commentNum = commentDomainService.countCommentByUserIdInActiveQuestion(targetUser.getId(),
                    CommentStatus.ACTIVE);

            communityCount = questionNum + commentNum;

            imgList = itemDomainService.getRecentTop2Item(targetUser).stream()
                    .map(item -> itemImgDomainService.findMainImg(item.getId()).getItemImgUrl()).toList();

            itemCount = itemDomainService.countByUserIdAndItemStatus(targetUser.getId(), ItemStatus.ACTIVE);

        } else { // 특정 유저일때
            targetUser = userDomainService.findById(targetId);
        }

        Boolean followStatus = followDomainService.getFollowStatus(user, targetUser.getId());
        Long followerCount = followDomainService.getFollowerCount(targetUser);
        Long followingCount = followDomainService.getFollowingCount(targetUser);

        return UserMypageResDto.of(targetUser, followStatus, followerCount, followingCount, itemCount, imgList,
                communityCount);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getUserItem(Long userId, Long targetId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Item> itemPage = itemDomainService.getUserAllItem(targetId, pageable);

        List<ItemSimpleDto> content = itemPage.stream()
                .map(item -> itemHelper.convertItemToSimpleResDto(item, user))
                .toList();

        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ClosetResponse> getUserCloset(Long userId, Long targetId, Pageable pageable) {
        Page<Closet> closetPage;
        User user = userDomainService.findById(userId);

        // User 일치 여부에 따라 조회하는 Public Closet만 조회할지 결정
        if (user.getId().equals(targetId)) {
            closetPage = closetDomainService.getUserAllCloset(targetId, pageable);
        } else {
            closetPage = closetDomainService.getUserAllPublicCloset(targetId, pageable);
        }

        List<ClosetResponse> content = closetPage.stream()
                .map(closet -> ClosetResponse.of(closet, itemScrapDomainService.countByClosetId(closet.getId())))
                .toList();

        return PaginationResponse.create(closetPage, content);
    }

    @Transactional
    public void patchUserProfileImg(Long userId, UserProfileImgReqDto dto) {
        User user = userDomainService.findById(userId);
        log.info("User Profile Img Change. User: {}", user.getId());
        log.info("Change URL: {}", dto.getImgUrl());
        user.changeProfileImgUrl(dto.getImgUrl());
        userDomainService.saveUser(user);
    }

    @Transactional
    public void deleteUserProfileImg(Long userId) {
        User user = userDomainService.findById(userId);
        log.info("User Profile Img Delete. User: {}", user.getId());
        user.changeProfileImgUrl(null);
        userDomainService.saveUser(user);
    }

    /**
     * 현재 유저가 업로드한 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResponse<ItemSimpleDto> getUserUploadItem(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        // 현재 유저가 업로드한 아이템 조회
        Page<Item> itemPage = itemDomainService.getUserAllItem(user.getId(), pageable);
        // content 제작
        List<ItemSimpleDto> content = itemPage.stream()
                .map(item -> itemHelper.convertItemToSimpleResDto(item, user)).toList();

        return new PaginationCountResponse<>(itemPage.hasNext(), itemPage.getNumber(), content,
                itemPage.getTotalElements());
    }

    /**
     * 현재 유저가 업로그한 Question 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResponse<QuestionSimpleResDto> getUserUploadQuestion(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Question> questionPage = questionDomainService.getUserAllQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream()
                .map(questionDtoMapper::dtoBuildByQuestionType)
                .toList();

        return new PaginationCountResponse<>(questionPage.hasNext(), questionPage.getNumber(), content,
                questionPage.getTotalElements());
    }

    /**
     * 현재 유저가 업로그한 Comment 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResponse<CommentSimpleResponse> getUserUploadComment(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Comment> commentPage = commentDomainService.getUserAllComment(user, pageable);

        List<CommentSimpleResponse> content = commentPage.stream().map(CommentSimpleResponse::of).toList();

        return new PaginationCountResponse<>(commentPage.hasNext(), commentPage.getNumber(), content,
                commentPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<UserSearchInfoDto> getHotSluver(Long userId, Long celebId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<User> userList = userDomainService.getHotSluver(celebId);

        return userList.stream()
                .map(hotSluver -> {
                    boolean isMine = user != null && Objects.equals(hotSluver.getId(), user.getId());
                    Boolean followStatus = followDomainService.getFollowStatus(user, hotSluver.getId());
                    return UserSearchInfoDto.of(hotSluver, followStatus, isMine);
                }).toList();
    }

    @Transactional
    public UserTermsResDto postTerms(Long userId) {
        User user = userDomainService.findById(userId);
        user.changeTermStatus(!user.getTermsStatus());
        userDomainService.saveUser(user);
        return UserTermsResDto.of(user);
    }

    @Transactional
    public void withdrawUser(Long userId, UserWithdrawReqDto dto) {
        User user = userDomainService.findById(userId);
        user.changeUserStatus(UserStatus.DELETED);
        userDomainService.saveUser(user);

        userWithdrawDataService.withdrawItemByUserId(user.getId());
        userWithdrawDataService.withdrawQuestionByUserId(user.getId());
        userWithdrawDataService.withdrawFollowByUserId(user.getId());
        userWithdrawDataService.withdrawCelebByUserId(user.getId());
        userWithdrawDataService.withdrawUserByUserId(user.getId());

        userWithdrawDomainService.saveUserWithdraw(user, dto.getReason(), dto.getContent());
        webHookService.sendWithdrawMessage(user);

    }

    @Transactional(readOnly = true)
    public UserSocialDto getUserSocialData(Long userId) {
        User user = userDomainService.findById(userId);
        return UserSocialDto.of(user);
    }

    @Transactional(readOnly = true)
    public UserTermsResDto findUserTermsStatus(Long userId) {
        User user = userDomainService.findById(userId);
        return UserTermsResDto.of(user);
    }

}
