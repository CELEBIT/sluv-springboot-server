package com.sluv.domain.user.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.user.enums.UserWithdrawReason;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_withdraw")
public class UserWithdraw extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_withdraw_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    @Column(name = "user_withdraw_reason")
    @Enumerated(EnumType.STRING)
    private UserWithdrawReason userWithdrawReason;

    @Column(name = "content")
    private String content;

    public static UserWithdraw toEntity(User user, UserWithdrawReason reason, String content) {
        return UserWithdraw.builder()
                .user(user)
                .userWithdrawReason(reason)
                .content(content)
                .build();
    }
}
