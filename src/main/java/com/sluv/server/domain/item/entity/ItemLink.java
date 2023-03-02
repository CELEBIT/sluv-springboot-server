package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemImgOrLinkStatus;
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
@Table(name = "item_link")
public class ItemLink extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_link_id")
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    @Size(max = 100)
    private String linkName;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemLinkUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus;

    @Builder
    public ItemLink(Long id, Long itemId, String linkName, String itemLinkUrl, ItemImgOrLinkStatus itemImgOrLinkStatus) {
        this.id = id;
        this.itemId = itemId;
        this.linkName = linkName;
        this.itemLinkUrl = itemLinkUrl;
        this.itemImgOrLinkStatus = itemImgOrLinkStatus;
    }
}
