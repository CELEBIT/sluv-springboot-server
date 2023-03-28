package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.dto.CelebParentResDto;
import com.sluv.server.domain.celeb.dto.CelebChildResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
    public List<CelebParentResDto> getInterestedCeleb(User user) {
        List<Celeb> interestedCelebs = celebRepository.findInterestedCeleb(user);
        return interestedCelebs.stream()
                .collect(Collectors.groupingBy(Celeb::getParent))
                .entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getCelebNameKr()))
                .map(entry -> CelebParentResDto.builder()
                        .id(entry.getKey().getId())
                        .celebNameKr(entry.getKey().getCelebNameKr())
                        .celebNameEn(entry.getKey().getCelebNameEn())
                        .subCelebList(entry.getValue().stream()
                                .map(child -> CelebChildResDto.builder()
                                        .id(child.getId())
                                        .celebNameKr(child.getCelebNameKr())
                                        .celebNameEn(child.getCelebNameEn())
                                        .build()
                                ).collect(Collectors.toList())
                        ).build()
                ).toList();


    }
}
