package com.sluv.server.domain.item.entity;

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
@Table(name = "place_rank")
public class PlaceRank extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_rank_id")
    private Long id;
    @NotNull
    @Size(max = 255)
    private String place;

    @Builder
    public PlaceRank(Long id, String place) {
        this.id = id;
        this.place = place;
    }
}
