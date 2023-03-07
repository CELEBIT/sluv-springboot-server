package com.sluv.server.domain.user.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class Follow extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @NotNull
    private Long followerId;

    @NotNull
    private Long followeeId;

    @Builder
    public Follow(Long id, Long followerId, Long followeeId) {
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
