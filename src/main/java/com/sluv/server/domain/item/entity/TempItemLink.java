package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.dto.ItemLinkResDto;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.Buffer;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    public static TempItemLink toEntity(TempItem tempItem, ItemLinkResDto dto) {
        return TempItemLink.builder()
                .tempItem(tempItem)
                .linkName(dto.getLinkName())
                .tempItemLinkUrl(dto.getItemLinkUrl())
                .build();
    }
}
