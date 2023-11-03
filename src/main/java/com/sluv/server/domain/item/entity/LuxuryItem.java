package com.sluv.server.domain.item.entity;

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
@Table(name = "luxury_item")
public class LuxuryItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "luxury_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    public static LuxuryItem toEntity(Item item) {
        return LuxuryItem.builder()
                .item(item)
                .build();
    }
}
