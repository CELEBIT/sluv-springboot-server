package com.sluv.server.domain.brand.entity;

import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "brand")
public class Brand extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String brandKr;

    @NotNull
    @Size(max = 300)
    private String brandEn;

    @Builder
    public Brand(Long id, String brandKr, String brandEn) {
        this.id = id;
        this.brandKr = brandKr;
        this.brandEn = brandEn;
    }
}
