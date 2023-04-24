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
@Table(name = "recent_select_celeb")
public class RecentSelectCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_select_celeb_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    @ManyToOne
    @JoinColumn(name = "new_celeb_id")
    private NewCeleb newCeleb;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Builder
    public RecentSelectCeleb(Long id, Celeb celeb, NewCeleb newCeleb, User user) {
        this.id = id;
        this.celeb = celeb;
        this.newCeleb = newCeleb;
        this.user = user;
    }
}
