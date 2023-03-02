package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemReportReason;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item_report")
public class ItemReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_report_id")
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    private Long reporterId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private ItemReportReason itemReportReason;

    @Size(max = 1002)
    private String content;


    @Builder
    public ItemReport(Long id, Long itemId, Long reporterId, ItemReportReason itemReportReason, String content) {
        this.id = id;
        this.itemId = itemId;
        this.reporterId = reporterId;
        this.itemReportReason = itemReportReason;
        this.content = content;
    }
}
