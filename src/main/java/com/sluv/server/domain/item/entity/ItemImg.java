package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.dto.ItemImgResDto;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_img")
public class ItemImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemImgUrl;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    private Integer sortOrder;


    public static ItemImg toEntity(Item item, ItemImgResDto dto){
        return ItemImg.builder()
                .item(item)
                .itemImgUrl(dto.getImgUrl())
                .representFlag(dto.getRepresentFlag())
                .sortOrder(dto.getSortOrder())
                .build();
    }
}
