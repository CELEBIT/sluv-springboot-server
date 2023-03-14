package com.sluv.server.domain.user.entity;

import com.sluv.server.domain.user.enums.UserReportReason;
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
@Table(name = "user_report_stack")
public class UserReportStack extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_stack_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_id")
    @NotNull
    private User reported;


    @Builder
    public UserReportStack(Long id, User reported) {
        this.id = id;
        this.reported = reported;
    }
}
