package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.comment.entity.CommentReport;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "item_category_item_category_id")
    private ItemCategory parent;

    @Builder
    public ItemCategory(Long id, String name, ItemCategory parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
}
