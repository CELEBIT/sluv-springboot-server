package com.sluv.server.domain.item.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "week_hot_item")
public class WeekHotItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "week_hot_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    public static WeekHotItem toEntity(Item item) {
        return WeekHotItem.builder()
                .item(item)
                .build();
    }
}
