package com.sluv.server.domain.brand.mapper;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.enums.NewBrandStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewBrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brandName", source = "newBrandPostReqDto.newBrandName")
    @Mapping(target = "newBrandStatus", source = "newBrandStatus", defaultValue = "ACTIVE")
    NewBrand toEntity(NewBrandPostReqDto newBrandPostReqDto, NewBrandStatus newBrandStatus);

    @Mapping(target = "newBrandId", source = "newBrand.id")
    @Mapping(target = "newBrandName", source = "newBrand.brandName")
    NewBrandPostResDto toNewBrandPostResDto(NewBrand newBrand);

}
