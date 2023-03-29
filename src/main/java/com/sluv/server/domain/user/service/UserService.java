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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
                .collect(Collectors.partitioningBy(c -> c.getParent() == null))
                .entrySet().stream()
                // parent가 있는 Celeb인지 분류
                .flatMap(entry ->{
                    // parent가 없는 Celeb
                    if (entry.getKey()) {
                        return entry.getValue().stream().map(data -> CelebParentResDto.builder()
                                .id(data.getId())
                                .celebNameKr(data.getCelebNameKr())
                                .celebNameEn(data.getCelebNameEn())
                                .subCelebList(null)
                                .build()
                        );
                    } else {
                        // parent가 있는 Celeb
                        return entry.getValue().stream()
                                .collect(Collectors.groupingBy(Celeb::getParent))
                                .entrySet().stream()
                                .map(subEntry -> CelebParentResDto.builder()
                                        .id(subEntry.getKey().getId())
                                        .celebNameKr(subEntry.getKey().getCelebNameKr())
                                        .celebNameEn(subEntry.getKey().getCelebNameEn())
                                        .subCelebList(
                                                subEntry.getValue().stream()
                                                        .map(child -> CelebChildResDto.builder()
                                                                .id(child.getId())
                                                                .celebNameKr(child.getCelebNameKr())
                                                                .celebNameEn(child.getCelebNameEn())
                                                                .build()
                                                        ).toList()
                                        ).build()
                                );
                    }
                }).toList();

    }
}
