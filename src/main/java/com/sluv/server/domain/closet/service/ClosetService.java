package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClosetService {
    private final ClosetRepository closetRepository;

    public void postBasicCloset(User user){

        // 기본 Url 지정이 필요
        String DEFAULT_COVER_IMG_URL = "def";

        Closet defCloset = Closet.builder()
                .user(user)
                .name("기본 옷장")
                .coverImgUrl(DEFAULT_COVER_IMG_URL)
                .basicFlag(true)
                .closetStatus(ClosetStatus.PRIVATE)
                .build();

        closetRepository.save(defCloset);
    }
}
