package com.sluv.domain.item.entity;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.item.dto.ItemSaveDto;
import com.sluv.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Builder.Default
    private List<ItemImg> itemImgs = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Builder.Default
    private List<ItemLink> itemLinks = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Builder.Default
    private List<ItemHashtag> itemHashtags = new ArrayList<>();

    /**
     * Item 생성
     */
    public static Item toEntity(User user, Celeb celeb, NewCeleb newCeleb,
                                ItemCategory itemCategory, Brand brand, NewBrand newBrand,
                                ItemSaveDto dto) {
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
                                ItemSaveDto dto, Long viewNum) {
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
                .viewNum(viewNum)
                .color(null)
                .build();
    }

    public void increaseViewNum() {
        this.viewNum++;
    }

    public void changeStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void changeItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
}
