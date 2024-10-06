package com.sluv.domain.visit.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "daily_visit")
public class DailyVisit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_visit_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static DailyVisit of(User user) {
        return DailyVisit.builder()
                .user(user)
                .build();
    }
}