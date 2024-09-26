package com.sluv.api.fixture;

import static com.sluv.domain.item.enums.ItemStatus.ACTIVE;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.user.entity.User;
import java.time.LocalDateTime;

public class ItemFixture {

    public static Item 기본_아이템_생성(User user, Celeb celeb, NewCeleb newCeleb,
                                 ItemCategory itemCategory, Brand brand, NewBrand newBrand,
                                 String name, LocalDateTime when, String where,
                                 String additionalInfo, String infoSource) {
        return Item.builder()
                .user(user)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .category(itemCategory)
                .brand(brand)
                .newBrand(newBrand)
                .name(name)
                .whenDiscovery(when)
                .whereDiscovery(where)
                .price(0)
                .color(null)
                .additionalInfo(additionalInfo)
                .infoSource(infoSource)
                .itemStatus(ACTIVE)
                .build();
    }

    public static ItemCategory 아이템_카테고리_생성(String name, ItemCategory parent) {
        return ItemCategory.builder()
                .name(name)
                .parent(parent)
                .build();
    }

}
