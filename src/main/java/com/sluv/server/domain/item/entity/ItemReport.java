package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.dto.ItemReportReqDto;
import com.sluv.server.domain.item.enums.ItemReportReason;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_report")
public class ItemReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @NotNull
    private User reporter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemReportReason itemReportReason;

    @Size(max = 1002)
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.WAITING;

    public static ItemReport toEntity(Item item, User user, ItemReportReqDto dto) {
        return ItemReport.builder()
                .item(item)
                .reporter(user)
                .itemReportReason(dto.getReason())
                .content(dto.getContent())
                .build();
    }
}
