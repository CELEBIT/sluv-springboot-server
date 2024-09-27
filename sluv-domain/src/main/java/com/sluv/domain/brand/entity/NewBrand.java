package com.sluv.domain.brand.entity;

import com.sluv.domain.brand.enums.NewBrandStatus;
import com.sluv.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "new_brand")
public class NewBrand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_brand_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String brandName;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private NewBrandStatus newBrandStatus;

    public static NewBrand toEntity(String newBrandName) {
        return NewBrand.builder()
                .brandName(newBrandName)
                .newBrandStatus(NewBrandStatus.ACTIVE)
                .build();
    }
}
