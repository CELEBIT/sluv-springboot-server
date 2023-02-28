package com.sluv.server.domain.user.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column
    @ColumnDefault("PENDING-PROFILE")
    private String status;

    @Column
    private String ageRange;

    @Column
    private String gender;


    @Builder
    public User(Long id, @NonNull String email, String nickname,
                @NonNull String snsType, String profileImgUrl,
                String status, String ageRange, String gender) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.snsType = snsType;
        this.profileImgUrl = profileImgUrl;
        this.status = status;
        this.ageRange = ageRange;
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
