package com.sluv.server.domain.user.entity;

import com.sluv.server.domain.user.enums.SnsType;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "user")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Size(max = 320)
    private String email;

    @Size(max = 45)
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private SnsType snsType;

    @Column(columnDefinition = "TEXT")
    private String profileImgUrl;

    @Size(max = 45)
    private String ageRange;

    @Size(max = 45)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PENDING_PROFILE'")
    private UserStatus userStatus;

    @Builder
    public User(Long id, String email, String nickname, SnsType snsType, String profileImgUrl, String ageRange, String gender, UserStatus userStatus) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.snsType = snsType;
        this.profileImgUrl = profileImgUrl;
        this.ageRange = ageRange;
        this.gender = gender;
        this.userStatus = userStatus;
    }
}
