package com.sluv.server.domain.user.entity;

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
@Table(name = "follow")
public class Follow extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @NotNull
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    @NotNull
    private User followee;

    public static Follow toEntity(User follower, User followee){
        return Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
    }
}
