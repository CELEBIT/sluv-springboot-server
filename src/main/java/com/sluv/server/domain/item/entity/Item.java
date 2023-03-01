package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.closet.enums.ClosetStatus;
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
@Table(name = "item")
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @NotNull
    private Long userId;

    private Long celebId;

    private Long newCelebId;

    @NotNull
    private Long categoryId;

    private Long brandId;

    private Long newBrandId;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus;

    @Builder

    public Item(Long id, Long userId, Long celebId, Long newCelebId,
                Long categoryId, Long brandId, Long newBrandId,
                String name, LocalDateTime whenDiscovery, String whereDiscovery,
                int price, String color, String additionalInfo, ItemStatus itemStatus) {
        this.id = id;
        this.userId = userId;
        this.celebId = celebId;
        this.newCelebId = newCelebId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.newBrandId = newBrandId;
        this.name = name;
        this.whenDiscovery = whenDiscovery;
        this.whereDiscovery = whereDiscovery;
        this.price = price;
        this.color = color;
        this.additionalInfo = additionalInfo;
        this.itemStatus = itemStatus;
    }
}
