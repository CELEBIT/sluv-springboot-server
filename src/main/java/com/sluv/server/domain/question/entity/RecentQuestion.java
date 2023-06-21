package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "recent_question")
public class RecentQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    private String qType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

}

