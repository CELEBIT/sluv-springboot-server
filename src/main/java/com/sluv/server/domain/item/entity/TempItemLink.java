package com.sluv.server.domain.item.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "temp_item_link")
public class TempItemLink extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_item_link_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "temp_item_id")
    @NotNull
    private TempItem tempItem;
    @Size(max = 100)
    private String linkName;

    @Column(columnDefinition = "TEXT")
    private String tempItemLinkUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    @Builder
    public TempItemLink(Long id, TempItem tempItem, String linkName, String tempItemLinkUrl, ItemImgOrLinkStatus itemImgOrLinkStatus) {
        this.id = id;
        this.tempItem = tempItem;
        this.linkName = linkName;
        this.tempItemLinkUrl = tempItemLinkUrl;
        this.itemImgOrLinkStatus = itemImgOrLinkStatus;
    }
}
