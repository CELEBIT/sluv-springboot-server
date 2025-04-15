package com.sluv.domain.celeb.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "interested_celeb")
public class InterestedCeleb extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_celeb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celeb_id")
    @Nullable
    private Celeb celeb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_celeb_id")
    @Nullable
    private NewCeleb newCeleb;

    public static InterestedCeleb toEntity(User user, Celeb celeb, NewCeleb newCeleb) {
        return InterestedCeleb.builder()
                .user(user)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .build();
    }

}
