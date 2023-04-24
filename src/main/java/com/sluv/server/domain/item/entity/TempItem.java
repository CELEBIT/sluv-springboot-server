package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "temp_item")
public class TempItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    @ManyToOne
    @JoinColumn(name = "new_celeb_id")
    private NewCeleb newCeleb;
    @ManyToOne
    @JoinColumn(name = "item_category_id")
    private ItemCategory category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "new_brand_id")
    private NewBrand newBrand;

    @Size(max = 100)
    private String name;

    private LocalDateTime whenDiscovery;

    @Size(max = 100)
    private String whereDiscovery;

    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(columnDefinition = "TEXT")
    private String infoSource;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus;

    @Builder
    public TempItem(Long id, User user, Celeb celeb, NewCeleb newCeleb, ItemCategory category, Brand brand, NewBrand newBrand, String name, LocalDateTime whenDiscovery, String whereDiscovery, Integer price, String additionalInfo, String infoSource, ItemStatus itemStatus) {
        this.id = id;
        this.user = user;
        this.celeb = celeb;
        this.newCeleb = newCeleb;
        this.category = category;
        this.brand = brand;
        this.newBrand = newBrand;
        this.name = name;
        this.whenDiscovery = whenDiscovery;
        this.whereDiscovery = whereDiscovery;
        this.price = price;
        this.additionalInfo = additionalInfo;
        this.infoSource = infoSource;
        this.itemStatus = itemStatus;
    }
}
