package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.CelebSearchByCategoryResDto;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb")
public class CelebController {
    private final CelebService celebService;

    @Operation(summary = "Celeb 검색", description = "입력한 이름으로 Celeb을 검색. Pagination 적용.")
    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<CelebSearchResDto>>> searchCelebByName(
            @RequestParam String celebName, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<CelebSearchResDto>>builder()
                        .result(celebService.searchCeleb(celebName, pageable))
                        .build()
        );
    }

    @Operation(summary = "관심셀럽 등록 시 Celeb 검색", description = "멤버 이름을 검색하면 그룹이 검색됨. Pagination X")
    @GetMapping("/search/interested")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchByCategoryResDto>>> searchInterestedCelebByName(
            @RequestParam String celebName) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchByCategoryResDto>>builder()
                        .result(celebService.searchInterestedCelebByName(celebName))
                        .build()
        );
    }

    @Operation(summary = "인기 셀럽 조회", description = "조회가 많이된 Celeb 상위 10개 조회")
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResDto>>> searchTop10Celeb() {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchResDto>>builder()
                        .result(celebService.getTop10Celeb())
                        .build()
        );
    }

    @Operation(summary = "관심 셀럽 조회 시, 카테고리별 셀럽 조회", description = "카테고리별 최대 30개를 한번에 전달, 카테고리는 순서 X, 셀럽는 가나다 순서.")
    @GetMapping("/category")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchByCategoryResDto>>> searchCelebByCategory() {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<CelebSearchByCategoryResDto>>builder()
                        .result(celebService.getCelebByCategory())
                        .build()
        );
    }
}
