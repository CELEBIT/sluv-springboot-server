package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.comment.entity.CommentImg;
import com.sluv.server.domain.comment.entity.CommentItem;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item")
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "celeb_celeb_id")
    private Celeb celeb;

    @ManyToOne
    @JoinColumn(name = "new_celeb_new_celeb_id")
    private NewCeleb newCeleb;

    @ManyToOne
    @JoinColumn(name = "item_category_item_category_id")
    @NotNull
    private ItemCategory category;

    @ManyToOne
    @JoinColumn(name = "brand_brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "new_brand_new_brand_id")
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

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item")
    List<CommentItem> commentItemList;

    @OneToMany(mappedBy = "item")
    List<ItemEditReq> itemEditReqList;

    @OneToMany(mappedBy = "item")
    List<ItemImg> itemImgList;

    @OneToMany(mappedBy = "item")
    List<ItemLike> itemLikeList;

    @OneToMany(mappedBy = "item")
    List<ItemLink> itemLinkList;

    @OneToMany(mappedBy = "item")
    List<ItemReport> itemReportList;

    @OneToMany(mappedBy = "item")
    List<ItemScrap> itemScrapList;

    @OneToMany(mappedBy = "item")
    List<RecentItem> recentItemList;

    @Builder
    public Item(Long id, User user, Celeb celeb, NewCeleb newCeleb, ItemCategory category, Brand brand, NewBrand newBrand, String name, LocalDateTime whenDiscovery, String whereDiscovery, int price, String color, String additionalInfo, String infoSource, ItemStatus itemStatus) {
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
        this.color = color;
        this.additionalInfo = additionalInfo;
        this.infoSource = infoSource;
        this.itemStatus = itemStatus;
    }
}
