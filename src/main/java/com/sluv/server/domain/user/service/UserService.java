package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebChildResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CelebRepository celebRepository;
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
}
