package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "temp_item")
public class TempItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_item_id")
    private Long id;

    @NotNull
    private Long userId;

    private Long celebId;

    private String newCelebName;

    private Long categoryId;

    private Long brandId;

    private String newBrandName;

    @Size(max = 100)
    private String name;

    private LocalDateTime whenDiscovery;

    @Size(max = 100)
    private String whereDiscovery;

    private int price;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(columnDefinition = "TEXT")
    private String infoSource;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus;

    @Builder
    public TempItem(Long id, Long userId, Long celebId, String newCelebName, Long categoryId, Long brandId, String newBrandName, String name, LocalDateTime whenDiscovery, String whereDiscovery, int price, String additionalInfo, String infoSource, ItemStatus itemStatus) {
        this.id = id;
        this.userId = userId;
        this.celebId = celebId;
        this.newCelebName = newCelebName;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.newBrandName = newBrandName;
        this.name = name;
        this.whenDiscovery = whenDiscovery;
        this.whereDiscovery = whereDiscovery;
        this.price = price;
        this.additionalInfo = additionalInfo;
        this.infoSource = infoSource;
        this.itemStatus = itemStatus;
    }
}
