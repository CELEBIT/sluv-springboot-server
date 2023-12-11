package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_celeb_id")
    private NewCeleb newCeleb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    @NotNull
    private ItemCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_brand_id")
    private NewBrand newBrand;
    @NotNull
    @Size(max = 100)
    private String name;

    private LocalDateTime whenDiscovery;

    @Size(max = 100)
    private String whereDiscovery;

    @NotNull
    private int price;

    @Size(max = 45)
    private String color;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(columnDefinition = "TEXT")
    private String infoSource;

    @Builder.Default
    @Column(name = "view_num")
    private Long viewNum = 0L;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus = ItemStatus.ACTIVE;

    /**
     * Item 생성
     */
    public static Item toEntity(User user, Celeb celeb, NewCeleb newCeleb,
                                ItemCategory itemCategory, Brand brand, NewBrand newBrand,
                                ItemPostReqDto dto) {
        return Item.builder()
                .user(user)
                .celeb(celeb)
                .category(itemCategory)
                .brand(brand)
                .newBrand(newBrand)
                .newCeleb(newCeleb)
                .name(dto.getItemName())
                .whenDiscovery(dto.getWhenDiscovery())
                .whereDiscovery(dto.getWhereDiscovery())
                .price(dto.getPrice())
                .additionalInfo(dto.getAdditionalInfo())
                .infoSource(dto.getInfoSource())
                .build();

    }

    /**
     * Item 수정
     */
    public static Item toEntity(Long id, User user, Celeb celeb, NewCeleb newCeleb,
                                ItemCategory itemCategory, Brand brand, NewBrand newBrand,
                                ItemPostReqDto dto) {
        return Item.builder()
                .id(id)
                .user(user)
                .celeb(celeb)
                .category(itemCategory)
                .brand(brand)
                .newBrand(newBrand)
                .newCeleb(newCeleb)
                .name(dto.getItemName())
                .whenDiscovery(dto.getWhenDiscovery())
                .whereDiscovery(dto.getWhereDiscovery())
                .price(dto.getPrice())
                .additionalInfo(dto.getAdditionalInfo())
                .infoSource(dto.getInfoSource())
                .build();

    }

    public void increaseViewNum() {
        this.viewNum++;
    }

    public void decreaseViewNum() {
        // 레디스 적용전 임시
        this.viewNum--;
    }

    public void changeStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
