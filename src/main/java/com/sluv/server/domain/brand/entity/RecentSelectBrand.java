package com.sluv.server.domain.brand.entity;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recent_select_brand")
public class RecentSelectBrand extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_select_brand_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "new_brand_id")
    private NewBrand newBrand;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;


    @Builder
    public RecentSelectBrand(Long id, Brand brand, NewBrand newBrand, User user) {
        this.id = id;
        this.brand = brand;
        this.newBrand = newBrand;
        this.user = user;
    }
}
