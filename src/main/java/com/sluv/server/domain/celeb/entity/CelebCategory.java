package com.sluv.server.domain.celeb.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "celeb_category")
public class CelebCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CelebCategory parent;

    @NotNull
    @Size(max = 45)
    private String name;

    @Builder
    public CelebCategory(Long id, CelebCategory parent, String name) {
        this.id = id;
        this.parent = parent;
        this.name = name;
    }
}
