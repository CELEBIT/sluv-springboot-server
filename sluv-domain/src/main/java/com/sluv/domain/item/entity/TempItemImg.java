package com.sluv.domain.item.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.common.enums.ItemImgOrLinkStatus;
import com.sluv.domain.item.dto.ItemImgDto;
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
@Table(name = "temp_item_img")
public class TempItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_item_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temp_item_id")
    @NotNull
    private TempItem tempItem;

    @Column(columnDefinition = "TEXT")
    private String tempItemImgUrl;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    private Integer sortOrder;


    public static TempItemImg toEntity(TempItem tempItem, ItemImgDto dto) {
        return TempItemImg.builder()
                .tempItem(tempItem)
                .tempItemImgUrl(dto.getImgUrl())
                .representFlag(dto.getRepresentFlag())
                .sortOrder(dto.getSortOrder())
                .build();
    }
}
