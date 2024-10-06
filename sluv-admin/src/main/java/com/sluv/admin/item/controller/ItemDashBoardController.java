package com.sluv.admin.item.controller;

import com.sluv.admin.common.response.SuccessDataResponse;
import com.sluv.admin.item.dto.HotItemResDto;
import com.sluv.admin.item.service.ItemService;
import com.sluv.admin.user.dto.UserCountByCategoryResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/backoffice/item/dashBoard")
public class ItemDashBoardController {

    private final ItemService itemService;

    @Operation(
            summary = "대시보드 - 유저의 연령대 분포",
            description = "대시보드에서 유저의 연령대 분포를 출력한다\n"
                    + "parentCategory == null -> 상위 카테고리로 조회\n"
                    + "parentCategory != null -> 하위 카테고리로 조회\n"
                    + "parentCategory -> 배우, 스포츠인, 방송인, 인플루언서"
    )
    @GetMapping("/celebCategory")
    public ResponseEntity<SuccessDataResponse<UserCountByCategoryResDto>> getItemCountByCelebCategory(
            @Nullable @RequestParam("parentCategory") String parentCategory) {
        UserCountByCategoryResDto result;
        if (parentCategory == null) {
            result = itemService.getItemCountByCelebCategoryParent();
        } else {
            result = itemService.getItemCountByCelebCategoryChild(parentCategory);
        }

        return ResponseEntity.ok().body(SuccessDataResponse.create(result));
    }

    @Operation(
            summary = "대시보드 - Top3 인기 아이템 조회",
            description = "대시보드에서 Top3 인기 아이템을 조회하여 출력\n"
                    + "인기의 기준은 (조회수 + 좋아요 수 + 스크랩 수)\n"
                    + "조회수 > 좋아요 > 스크랩 순서로 중요."
    )
    @GetMapping("/hotItem")
    public ResponseEntity<SuccessDataResponse<List<HotItemResDto>>> getTop3HotItem() {
        List<HotItemResDto> response = itemService.getTop3HotItem();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
