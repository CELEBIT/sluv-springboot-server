package com.sluv.api.celeb.controller;

import com.sluv.api.celeb.dto.response.CelebSearchByCategoryResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.celeb.service.CelebService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
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
    public ResponseEntity<SuccessDataResponse<PaginationResponse<CelebSearchResponse>>> searchCelebByName(
            @RequestParam String celebName, Pageable pageable) {
        PaginationResponse<CelebSearchResponse> response = celebService.searchCeleb(celebName, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "관심셀럽 등록 시 Celeb 검색", description = "멤버 이름을 검색하면 그룹이 검색됨. Pagination X")
    @GetMapping("/search/interested")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchByCategoryResponse>>> searchInterestedCelebByName(
            @RequestParam String celebName) {
        List<CelebSearchByCategoryResponse> response = celebService.searchInterestedCelebByName(celebName);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "인기 셀럽 조회", description = "조회가 많이된 Celeb 상위 10개 조회")
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResponse>>> searchTop10Celeb() {
        List<CelebSearchResponse> response = celebService.getTop10Celeb();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "관심 셀럽 조회 시, 카테고리별 셀럽 조회", description = "카테고리별 최대 30개를 한번에 전달, 카테고리는 순서 X, 셀럽는 가나다 순서.")
    @GetMapping("/category")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchByCategoryResponse>>> searchCelebByCategory() {
        List<CelebSearchByCategoryResponse> response = celebService.getCelebByCategory();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
