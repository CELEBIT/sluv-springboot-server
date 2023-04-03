package com.sluv.server.domain.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ItemPostReqDto {
    /**
     * 착용사진, 셀럽, 아이템 종류, 브랜드, 상품명, 금액대
     * 날짜, 장소, 추가 정보, 구매 링크
     */

    private List<Map<Long, String>> imgList;
    private Long celebId;
    private LocalDateTime whenDiscovery;
    private String whereDiscovery;
    private Long categoryId;
    private Long brandId;
    private String itemName;
    private Integer price;

    private String additionalInfo;
    private List<Long> hashTagIdList;
    private List<Map<String, String>> infoSourceList;

}
