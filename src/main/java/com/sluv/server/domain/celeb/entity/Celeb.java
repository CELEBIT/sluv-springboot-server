package com.sluv.server.domain.celeb.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.server.domain.celeb.enums.CelebStatus;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"id", "parent", "celebCategory", "celebNameKr", "celebNameEn", "celebStatus", "created_at",
        "updated_at"})
@Table(name = "celeb")
public class Celeb extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Celeb parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celeb_category_id")
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

    @OneToMany(mappedBy = "parent")
    private List<Celeb> subCelebList = new ArrayList<>();

    @Builder
    public Celeb(Long id, Celeb parent, CelebCategory celebCategory, String celebNameKr, String celebNameEn,
                 CelebStatus celebStatus) {
        this.id = id;
        this.parent = parent;
        this.celebCategory = celebCategory;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
        this.celebStatus = celebStatus;
    }
}
