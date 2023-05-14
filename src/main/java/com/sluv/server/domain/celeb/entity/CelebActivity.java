package com.sluv.server.domain.celeb.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "celeb_activity")
public class CelebActivity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_activity_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    private String activityName;

    @Builder
    public CelebActivity(Long id, Celeb celeb, String activityName) {
        this.id = id;
        this.celeb = celeb;
        this.activityName = activityName;
    }
}
