package com.sluv.domain.user.entity;

import com.sluv.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_report_stack")
public class UserReportStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    @NotNull
    private User reported;


    public static UserReportStack toEntity(User reported) {
        return UserReportStack.builder()
                .reported(reported)
                .build();
    }
}
