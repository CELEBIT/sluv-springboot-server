package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebChildResDto;
import com.sluv.server.domain.celeb.repository.InterestedCelebRepository;
import com.sluv.server.domain.user.dto.UserProfileReqDto;
import com.sluv.server.domain.user.dto.UserReportReqDto;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.entity.UserReport;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.exception.UserNicknameDuplicatedException;
import com.sluv.server.domain.user.exception.UserReportDuplicateException;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserReportRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.enums.ReportStatus;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CelebRepository celebRepository;
    private final FollowRepository followRepository;
    private final UserReportRepository userReportRepository;
    private final InterestedCelebRepository interestedCelebRepository;

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
}
