package com.sluv.server.domain.celeb.entity;

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
@Table(name = "interested_celeb")
public class InterestedCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_celeb_id")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long celebId;

    @Builder
    public InterestedCeleb(Long id, Long userId, Long celebId) {
        this.id = id;
        this.userId = userId;
        this.celebId = celebId;
    }
}
