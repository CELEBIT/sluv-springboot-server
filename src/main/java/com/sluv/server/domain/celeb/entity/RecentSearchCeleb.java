package com.sluv.server.domain.celeb.entity;

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
@Table(name = "recent_search_celeb")
public class RecentSearchCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_search_celeb_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "celeb_celeb_id")
    @NotNull
    private Celeb celeb;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    @NotNull
    private User user;

    @Builder
    public RecentSearchCeleb(Long id, Celeb celeb, User user) {
        this.id = id;
        this.celeb = celeb;
        this.user = user;
    }
}
