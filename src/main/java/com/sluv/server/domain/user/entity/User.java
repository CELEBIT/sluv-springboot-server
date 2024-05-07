package com.sluv.server.domain.user.entity;

import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @NotNull
    @Size(max = 320)
    private String email;
    @Size(max = 45)
    private String nickname;
    @NotNull
    @Enumerated(EnumType.STRING)
//    @Size(max = 45) 추후 변경 필요
    private SnsType snsType;
    @Column(columnDefinition = "TEXT")
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private UserAge ageRange;

    @Enumerated(EnumType.STRING)
    private UserGender gender;
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PENDING_PROFILE'")
    private UserStatus userStatus;

    private Boolean termsStatus;

    public static User toEntity(SocialUserInfoDto userInfoDto, SnsType snsType) {
        return User.builder()
                .email(userInfoDto.getEmail())
                .snsType(snsType)
                .profileImgUrl(userInfoDto.getProfileImgUrl())
                .ageRange(userInfoDto.getAgeRange())
                .gender(userInfoDto.getGender())
                .userStatus(UserStatus.PENDING_PROFILE)
                .termsStatus(false)
                .build();
    }

    public static User toDeletedUser(User user) {
        String nicknameHashCode = String.valueOf(user.nickname.hashCode());
        return User.builder()
                .id(user.getId())
                .email(nicknameHashCode + "@" + nicknameHashCode + "-withdraw.sluv")
                .nickname(nicknameHashCode)
                .snsType(user.getSnsType())
                .profileImgUrl(null)
                .ageRange(user.getAgeRange())
                .gender(user.getGender())
                .userStatus(user.getUserStatus())
                .termsStatus(user.getTermsStatus())
                .build();

    }

    public void changeProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void changeTermStatus(Boolean termsStatus) {
        this.termsStatus = termsStatus;
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
