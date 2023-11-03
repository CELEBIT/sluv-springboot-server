package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.dto.PlaceRankReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "place_rank")
public class PlaceRank extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_rank_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    @Size(max = 255)
    private String place;

    public static PlaceRank toEntity(User user, PlaceRankReqDto dto) {
        return PlaceRank.builder()
                .user(user)
                .place(dto.getPlaceName())
                .build();
    }
}
