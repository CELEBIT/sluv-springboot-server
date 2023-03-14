package com.sluv.server.domain.brand.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recent_brand")
public class RecentBrand extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_brand_id")
    private Long id;

    @NotNull
    private Long brandId;

    @NotNull
    private Long userId;


    @Builder
    public RecentBrand(Long id, Long brandId, Long userId) {
        this.id = id;
        this.brandId = brandId;
        this.userId = userId;
    }
}
