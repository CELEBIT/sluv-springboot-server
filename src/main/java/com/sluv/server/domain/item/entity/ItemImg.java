package com.sluv.server.domain.item.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item_img")
public class ItemImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemImgUrl;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    private Integer sortOrder;


    @Builder
    public ItemImg(Long id, Item item, String itemImgUrl, Boolean representFlag, ItemImgOrLinkStatus itemImgOrLinkStatus, Integer sortOrder) {
        this.id = id;
        this.item = item;
        this.itemImgUrl = itemImgUrl;
        this.representFlag = representFlag;
        this.itemImgOrLinkStatus = itemImgOrLinkStatus;
        this.sortOrder = sortOrder;
    }
}
