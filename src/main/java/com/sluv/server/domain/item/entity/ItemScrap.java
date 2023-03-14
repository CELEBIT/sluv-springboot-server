package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item_scrap")
public class ItemScrap extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_scrap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "closet_closet_id")
    @NotNull
    private Closet closet;


    @Builder
    public ItemScrap(Long id, Item item, Closet closet) {
        this.id = id;
        this.item = item;
        this.closet = closet;
    }
}
