package com.sluv.server.domain.brand.mapper;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandSearchResDto toBrandSearchResDto(Brand brand);

}