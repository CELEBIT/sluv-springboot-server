package com.sluv.server.domain.celeb.entity;

import com.sluv.server.domain.celeb.enums.CelebStatus;
import com.sluv.server.domain.comment.entity.CommentReport;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "celeb")
public class Celeb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "celeb_celeb_id")
    private Celeb parentCeleb;

    @ManyToOne
    @JoinColumn(name = "celeb_category_celeb_category_id")
    @NotNull
    private CelebCategory celebCategory;

    @NotNull
    @Size(max = 255)
    private String celebNameKr;

    @NotNull
    @Size(max = 255)
    private String celebNameEn;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private CelebStatus celebStatus;

    @OneToMany(mappedBy = "celeb")
    List<InterestedCeleb> interestedCelebList;

    @OneToMany(mappedBy = "parentCeleb")
    List<Celeb> celebList;

    @OneToMany(mappedBy = "celeb")
    List<RecentSearchCeleb> recentSearchCelebList;

    @OneToMany(mappedBy = "celeb")
    List<Item> itemList;

    @Builder
    public Celeb(Long id, Celeb parentCeleb, CelebCategory celebCategory, String celebNameKr, String celebNameEn, CelebStatus celebStatus) {
        this.id = id;
        this.parentCeleb = parentCeleb;
        this.celebCategory = celebCategory;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
        this.celebStatus = celebStatus;
    }
}
