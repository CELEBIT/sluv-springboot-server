package com.sluv.domain.user.entity;

import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamicInsert
@Table(name = "`user`")
public class User extends BaseEntity {

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

    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private UserAge ageRange;

    @Enumerated(EnumType.STRING)
    private UserGender gender;
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PENDING_PROFILE'")
    private UserStatus userStatus;

    private Boolean termsStatus;

    private Boolean alarmStatus;

    public static User toEntity(SocialUserInfoDto userInfoDto, SnsType snsType, String fcm) {
        return User.builder()
                .email(userInfoDto.getEmail())
                .snsType(snsType)
                .profileImgUrl(userInfoDto.getProfileImgUrl())
                .fcmToken(fcm)
                .ageRange(userInfoDto.getAgeRange())
                .gender(userInfoDto.getGender())
                .userStatus(UserStatus.PENDING_PROFILE)
                .termsStatus(false)
                .alarmStatus(true)
                .build();
    }

    public static User toDeletedUser(User user) {
        String nicknameHashCode = String.valueOf(user.nickname.hashCode());
        return User.builder()
                .id(user.getId())
                .email(nicknameHashCode + "@" + nicknameHashCode + "-withdraw.sluv")
                .snsType(user.getSnsType())
                .profileImgUrl(null)
                .fcmToken(null)
                .ageRange(user.getAgeRange())
                .gender(user.getGender())
                .userStatus(user.getUserStatus())
                .termsStatus(user.getTermsStatus())
                .alarmStatus(user.alarmStatus)
                .build();
    }

    public static User toVisitUser() {
        return User.builder()
                .id(0L)
                .email("user@visitent-visit.sluv")
                .nickname("visitent")
                .snsType(SnsType.ETC)
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

    public void changeFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void changeAlarmStatus(Boolean alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
