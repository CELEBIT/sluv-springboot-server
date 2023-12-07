package com.sluv.server.domain.visit.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "visit_history")
public class VisitHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_history_id")
    private Long id;
    private LocalDateTime date;
    private Long visitCount;

    public static VisitHistory of(LocalDateTime date, Long visitCount) {
        return VisitHistory.builder()
                .date(date)
                .visitCount(visitCount)
                .build();
    }
}
