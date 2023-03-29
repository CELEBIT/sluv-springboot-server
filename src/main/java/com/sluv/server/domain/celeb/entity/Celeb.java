package com.sluv.server.domain.celeb.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.server.domain.celeb.enums.CelebStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"id", "parent", "celebCategory", "celebNameKr", "celebNameEn", "celebStatus", "created_at", "updated_at"})
@Table(name = "celeb")
public class Celeb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Celeb parent;

    @ManyToOne
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

    @Builder
    public Celeb(Long id, Celeb parent, CelebCategory celebCategory, String celebNameKr, String celebNameEn, CelebStatus celebStatus) {
        this.id = id;
        this.parent = parent;
        this.celebCategory = celebCategory;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
        this.celebStatus = celebStatus;
    }
}
