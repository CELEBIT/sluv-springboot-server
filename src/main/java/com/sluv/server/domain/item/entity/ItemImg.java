package com.sluv.server.domain.item.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ImgStatus;
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

    @NotNull
    private Long itemId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemImgUrl;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ImgStatus imgStatus;


    @Builder
    public ItemImg(Long id, Long itemId, String itemImgUrl, Boolean representFlag, ImgStatus imgStatus) {
        this.id = id;
        this.itemId = itemId;
        this.itemImgUrl = itemImgUrl;
        this.representFlag = representFlag;
        this.imgStatus = imgStatus;
    }
}
