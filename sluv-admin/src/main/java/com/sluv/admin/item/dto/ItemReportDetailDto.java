package com.sluv.admin.item.dto;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.item.enums.ItemReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemReportDetailDto {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    @Schema(description = "아이템 신고 이유(enums)")
    private ItemReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    @Schema(description = "item 이미지 리스트")
    private List<ItemImgResDto> imgList;
    @Schema(description = "item 링크 리스트")
    private List<ItemLinkResDto> linkList;
    private CelebSearchResDto celeb;
    private BrandSearchResDto brand;
    private ItemCategoryDto category;
    @Schema(description = "추가정보")
    private String additional_info;
    private String color;
    private String name;
    private Integer price;
    @Schema(description = "발견 시간 ex)2021-11-20T09:10:20")
    private LocalDateTime whenDiscovery;
    @Schema(description = "발견 장소")
    private String whereDiscovery;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ItemReportDetailDto of(ItemReport itemReport, List<ItemImgResDto> imgList, List<ItemLinkResDto> linkList) {
        return ItemReportDetailDto.builder()
                .reporterId(itemReport.getReporter().getId())
                .reporterNickname(itemReport.getReporter().getNickname())
                .reportedId(itemReport.getItem().getUser().getId())
                .reportedNickname(itemReport.getItem().getUser().getNickname())
                .reportId(itemReport.getId())
                .reportReason(itemReport.getItemReportReason())
                .content(itemReport.getContent())
                .reportStatus(itemReport.getReportStatus())
                .imgList(imgList)
                .linkList(linkList)
                .celeb(CelebSearchResDto.from(itemReport.getItem().getCeleb()))
                .brand(BrandSearchResDto.from(itemReport.getItem().getBrand()))
                .category(ItemCategoryDto.from(itemReport.getItem().getCategory()))
                .additional_info(itemReport.getItem().getAdditionalInfo())
                .color(itemReport.getItem().getColor())
                .name(itemReport.getItem().getName())
                .price(itemReport.getItem().getPrice())
                .whenDiscovery(itemReport.getItem().getWhenDiscovery())
                .whereDiscovery(itemReport.getItem().getWhereDiscovery())
                .createdAt(itemReport.getItem().getCreatedAt())
                .updatedAt(itemReport.getItem().getUpdatedAt())
                .build();

    }
}