package com.sluv.server.domain.closet.entity;

import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "closet")
public class Closet extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    @NotNull
    private User user;

    @NotNull
    @Size(max = 45)
    private String name;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String coverImgUrl;

    @NotNull
    @ColumnDefault("0")
    private Boolean basicFlag;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PUBLIC'")
    private ClosetStatus closetStatus;

    @Builder
    public Closet(Long id, User user, String name, String coverImgUrl, Boolean basicFlag, ClosetStatus closetStatus) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
        this.basicFlag = basicFlag;
        this.closetStatus = closetStatus;
    }
}
