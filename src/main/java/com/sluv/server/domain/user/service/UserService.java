package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebChildResDto;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
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
}
