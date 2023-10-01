package com.sluv.server.domain.celeb.entity;

import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static InterestedCeleb toEntity(User user, Celeb celeb){
        return InterestedCeleb.builder()
                .user(user)
                .celeb(celeb)
                .build();
    }

}
