package com.sluv.server.domain.item.entity;

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

    @NotNull
    private Long itemId;

    @NotNull
    private Long closetId;


    @Builder
    public ItemScrap(Long id, Long itemId, Long closetId) {
        this.id = id;
        this.itemId = itemId;
        this.closetId = closetId;
    }
}
