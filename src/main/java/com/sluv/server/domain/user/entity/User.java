package com.sluv.server.domain.user.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    @NonNull
    @Column
    private String email;
    @Column
    private String nickname;
    @NonNull
    @Column
    private String snsType;
    @Column
    private String profileImgUrl;
    @NonNull
    @Column(columnDefinition = "PENDING-PROFILE")
    private String status;


    @Builder
    public User(Long id, @NonNull String email,
                String nickname, @NonNull String snsType,
                String profileImgUrl, @NonNull String status) {

        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.snsType = snsType;
        this.profileImgUrl = profileImgUrl;
        this.status = status;
    }
}
