package com.sluv.server.domain.celeb.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recent_search_celeb")
public class RecentSearchCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_search_celeb_id")
    private Long id;

    @NotNull
    private Long celebId;

    @NotNull
    private Long userId;


    @Builder
    public RecentSearchCeleb(Long id, Long celebId, Long userId) {
        this.id = id;
        this.celebId = celebId;
        this.userId = userId;
    }
}
