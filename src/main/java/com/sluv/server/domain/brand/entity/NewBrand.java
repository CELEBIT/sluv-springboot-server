package com.sluv.server.domain.brand.entity;

import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.item.enums.ItemStatus;
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
@Table(name = "new_brand")
public class NewBrand extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_brand_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String brandName;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private NewBrandStatus newBrandStatus;

    @Builder
    public NewBrand(Long id, String brandName, NewBrandStatus newBrandStatus) {
        this.id = id;
        this.brandName = brandName;
        this.newBrandStatus = newBrandStatus;
    }
}
