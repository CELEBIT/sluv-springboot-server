package com.sluv.server.domain.brand.entity;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static NewBrand toEntity(NewBrandPostReqDto newBrandPostReqDto){
        return NewBrand.builder()
                .brandName(newBrandPostReqDto.getNewBrandName())
                .newBrandStatus(NewBrandStatus.ACTIVE)
                .build();
    }
}
