package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item_img")
public class ItemImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemImgUrl;

    @NotNull
    private Boolean representFlag;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus;


    @Builder
    public ItemImg(Long id, Long itemId, String itemImgUrl, Boolean representFlag, ItemImgOrLinkStatus itemImgOrLinkStatus) {
        this.id = id;
        this.itemId = itemId;
        this.itemImgUrl = itemImgUrl;
        this.representFlag = representFlag;
        this.itemImgOrLinkStatus = itemImgOrLinkStatus;
    }
}
