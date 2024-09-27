package com.sluv.domain.item.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.common.enums.ItemImgOrLinkStatus;
import com.sluv.domain.item.dto.ItemLinkDto;
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
@Table(name = "item_link")
public class ItemLink extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_link_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @NotNull
    @Size(max = 100)
    private String linkName;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemLinkUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;


    public static ItemLink toEntity(Item item, ItemLinkDto dto) {
        return ItemLink.builder()
                .item(item)
                .linkName(dto.getLinkName())
                .itemLinkUrl(dto.getItemLinkUrl())
                .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                .build();
    }
}
