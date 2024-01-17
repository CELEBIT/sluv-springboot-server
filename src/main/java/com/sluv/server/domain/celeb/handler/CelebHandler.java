package com.sluv.server.domain.celeb.handler;

import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CelebHandler {

    /**
     * Celeb 리스트를 CelebSearchResDto 리스트로 변경
     */
    public List<CelebSearchResDto> convertCelebSearchResDto(List<Celeb> celebList) {
        return celebList.stream()
                .map(CelebSearchResDto::of)
                .toList();
    }
}
