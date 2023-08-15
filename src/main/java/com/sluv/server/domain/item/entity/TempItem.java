package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.item.dto.TempItemPostReqDto;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus = ItemStatus.ACTIVE;

    public static TempItem toEntity(User user, Celeb celeb, NewCeleb newCeleb,
                                    ItemCategory category, Brand brand, NewBrand newBrand,
                                    TempItemPostReqDto dto) {
        return TempItem.builder()
                .user(user)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .category(category)
                .brand(brand)
                .newBrand(newBrand)
                .name(dto.getItemName())
                .whenDiscovery(dto.getWhenDiscovery())
                .whereDiscovery(dto.getWhereDiscovery())
                .price(dto.getPrice())
                .additionalInfo(dto.getAdditionalInfo())
                .infoSource(dto.getInfoSource())
                .build();
    }
}
