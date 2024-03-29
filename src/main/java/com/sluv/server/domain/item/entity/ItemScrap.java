package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.closet.entity.Closet;
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
@Table(name = "item_scrap")
public class ItemScrap extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_scrap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "closet_id")
    @NotNull
    private Closet closet;


    public static ItemScrap toEntity(Item item, Closet closet) {
        return ItemScrap.builder()
                .item(item)
                .closet(closet)
                .build();
    }

    public void changeCloset(Closet closet){
        this.closet = closet;
    }
}
