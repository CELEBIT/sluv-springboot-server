package com.sluv.server.domain.brand.mapper;

import com.sluv.server.domain.brand.dto.RecentSelectBrandResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RecentSelectBrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "newBrand", source = "newBrand")
    @Mapping(target = "user", source = "user")
    RecentSelectBrand toEntity(Brand brand, NewBrand newBrand, User user);

    @Mapping(target = "id", source = "recentSelectBrand", qualifiedByName = "mapId")
    @Mapping(target = "brandName", source = "recentSelectBrand", qualifiedByName = "mapBrandName")
    @Mapping(target = "brandImgUrl", source = "recentSelectBrand", qualifiedByName = "mapBrandUrl")
    @Mapping(target = "flag", source = "recentSelectBrand", qualifiedByName = "mapFlag")
    RecentSelectBrandResDto toRecentSelectBrandResDto(RecentSelectBrand recentSelectBrand);

    @Named("mapId")
    default Long mapId(RecentSelectBrand recentSelectBrand) {
        Long brandId;
        String flag = recentSelectBrand.getBrand() != null ? "Y" :"N";
        if(flag.equals("Y")){
            brandId = recentSelectBrand.getBrand().getId();
        }else{
            brandId = recentSelectBrand.getNewBrand().getId();
        }
        return brandId;
    }

    @Named("mapBrandName")
    static String mapBrandName(RecentSelectBrand recentSelectBrand) {
        String brandName;
        String flag = recentSelectBrand.getBrand() != null ? "Y" :"N";
        if(flag.equals("Y")){
            brandName = recentSelectBrand.getBrand().getBrandKr();
        }else{
            brandName = recentSelectBrand.getNewBrand().getBrandName();
        }
        return brandName;
    }

    @Named("mapBrandUrl")
    static String mapBrandUrl(RecentSelectBrand recentSelectBrand) {
        String brandImgUrl;
        String flag = recentSelectBrand.getBrand() != null ? "Y" :"N";
        if(flag.equals("Y")){
            brandImgUrl = recentSelectBrand.getBrand().getBrandImgUrl();
        }else{
            brandImgUrl = null;
        }
        return brandImgUrl;
    }

    @Named("mapFlag")
    static String mapFlag(RecentSelectBrand recentSelectBrand) {
        String flag = recentSelectBrand.getBrand() != null ? "Y" :"N";
        return flag;
    }
}
