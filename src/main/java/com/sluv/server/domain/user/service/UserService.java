package com.sluv.server.domain.user.service;

import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.dto.CommentSimpleResDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.helper.ItemHelper;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.item.service.ItemCacheService;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.mapper.QuestionDtoMapper;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.dto.UserMypageResDto;
import com.sluv.server.domain.user.dto.UserProfileImgReqDto;
import com.sluv.server.domain.user.dto.UserProfileReqDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.dto.UserTermsResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.exception.UserNicknameDuplicatedException;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationCountResDto;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ItemRepository itemRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ClosetRepository closetRepository;
    private final ItemHelper itemHelper;
    private final QuestionDtoMapper questionDtoMapper;
    private final ItemCacheService itemCacheService;

    @Transactional
    public void postUserProfile(User user, UserProfileReqDto dto) {
        User currentUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        itemCacheService.deleteAllItemCacheByUserId(user.getId());
        // 닉네임 중복 검사
        Boolean nicknameExistsStatus = userRepository.existsByNickname(dto.getNickName());
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
    public UserMypageResDto getUserMypage(User user, Long userId) {
        User targetUser;
        Long itemCount = null;
        List<String> imgList = null;
        Long communityCount = null;

        if (userId == null) { // 현재 유저일때
            targetUser = user;

            Long questionNum = questionRepository.countByUserId(targetUser.getId());
            Long commentNum = commentRepository.countByUserId(targetUser.getId());

            communityCount = questionNum + commentNum;

            imgList = itemRepository.getRecentTop2Item(targetUser).stream()
                    .map(item -> itemImgRepository.findMainImg(item.getId()).getItemImgUrl()).toList();

            itemCount = itemRepository.countByUserId(targetUser.getId());

        } else { // 특정 유저일때
            targetUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        }

        Boolean followStatus = followRepository.getFollowStatus(user, targetUser.getId());
        Long followerCount = followRepository.getFollowerCount(targetUser);
        Long followingCount = followRepository.getFollowingCount(targetUser);

        return UserMypageResDto.of(targetUser, followStatus, followerCount, followingCount, itemCount, imgList,
                communityCount);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getUserItem(User user, Long userId, Pageable pageable) {
        Page<Item> itemPage = itemRepository.getUserAllItem(userId, pageable);

        List<ItemSimpleResDto> content = itemPage.stream()
                .map(item -> itemHelper.convertItemToSimpleResDto(item, user))
                .toList();

        return PaginationResDto.of(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ClosetResDto> getUserCloset(User user, Long userId, Pageable pageable) {
        Page<Closet> closetPage;

        // User 일치 여부에 따라 조회하는 Public Closet만 조회할지 결정
        if (user.getId().equals(userId)) {
            closetPage = closetRepository.getUserAllCloset(userId, pageable);
        } else {
            closetPage = closetRepository.getUserAllPublicCloset(userId, pageable);
        }

        List<ClosetResDto> content = closetPage.stream()
                .map(closet -> ClosetResDto.of(closet, itemScrapRepository.countByClosetId(closet.getId()))).toList();

        return PaginationResDto.of(closetPage, content);
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
     * 현재 유저가 업로드한 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResDto<ItemSimpleResDto> getUserUploadItem(User user, Pageable pageable) {
        // 현재 유저가 업로드한 아이템 조회
        Page<Item> itemPage = itemRepository.getUserAllItem(user.getId(), pageable);
        // content 제작
        List<ItemSimpleResDto> content = itemPage.stream()
                .map(item -> itemHelper.convertItemToSimpleResDto(item, user)).toList();

        return new PaginationCountResDto<>(itemPage.hasNext(), itemPage.getNumber(), content,
                itemPage.getTotalElements());
    }

    /**
     * 현재 유저가 업로그한 Question 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResDto<QuestionSimpleResDto> getUserUploadQuestion(User user, Pageable pageable) {
        Page<Question> questionPage = questionRepository.getUserAllQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream()
                .map(questionDtoMapper::dtoBuildByQuestionType)
                .toList();

        return new PaginationCountResDto<>(questionPage.hasNext(), questionPage.getNumber(), content,
                questionPage.getTotalElements());
    }

    /**
     * 현재 유저가 업로그한 Comment 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResDto<CommentSimpleResDto> getUserUploadComment(User user, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getUserAllComment(user, pageable);

        List<CommentSimpleResDto> content = commentPage.stream().map(CommentSimpleResDto::of).toList();

        return new PaginationCountResDto<>(commentPage.hasNext(), commentPage.getNumber(), content,
                commentPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<UserSearchInfoDto> getHotSluver(User user, Long celebId) {
        List<User> userList = userRepository.getHotSluver(user, celebId);

        return userList.stream()
                .map(_user -> UserSearchInfoDto.of(_user, followRepository.getFollowStatus(user, _user.getId())))
                .toList();


    }

    public UserTermsResDto postTerms(User user) {
        user.changeTermStatus(!user.getTermsStatus());
        userRepository.save(user);
        return UserTermsResDto.builder()
                .termsStatus(user.getTermsStatus())
                .build();
    }
}
