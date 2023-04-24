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
@Table(name = "interested_celeb")
public class InterestedCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_celeb_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "celeb_id")
    @NotNull
    private Celeb celeb;

    @Builder
    public InterestedCeleb(Long id, User user, Celeb celeb) {
        this.id = id;
        this.user = user;
        this.celeb = celeb;
    }
}
