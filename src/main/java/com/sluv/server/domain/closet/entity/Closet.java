package com.sluv.server.domain.closet.entity;

import com.sluv.server.domain.closet.enums.ClosetStatus;
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
@Table(name = "closet")
public class Closet extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closet_id")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 45)
    private String name;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String coverImgUrl;

    @NotNull
    private Boolean isBasic;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PUBLIC'")
    private ClosetStatus closetStatus;

    @Builder
    public Closet(Long id, Long userId, String name, String coverImgUrl, Boolean isBasic, ClosetStatus closetStatus) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
        this.isBasic = isBasic;
        this.closetStatus = closetStatus;
    }
}
