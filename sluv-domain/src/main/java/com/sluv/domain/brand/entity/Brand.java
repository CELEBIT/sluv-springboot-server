package com.sluv.domain.brand.entity;

import com.sluv.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "brand")
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String brandKr;

    @NotNull
    @Size(max = 300)
    private String brandEn;

    @Column(columnDefinition = "TEXT")
    private String brandImgUrl;

    public static Brand of(String krName, String enName, String imageUrl) {
        return Brand.builder()
                .brandKr(krName)
                .brandEn(enName)
                .brandImgUrl(imageUrl)
                .build();
    }

}
