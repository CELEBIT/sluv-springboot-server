package com.sluv.server.domain.item.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "item_category")
public class ItemCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_category_id")
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    private Long parentId;

    @Builder
    public ItemCategory(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
