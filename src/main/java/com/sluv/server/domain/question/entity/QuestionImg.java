package com.sluv.server.domain.question.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ImgStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "question_img")
public class QuestionImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_img_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_question_id")
    @NotNull
    private Question question;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @Size(max = 100)
    private String description;

    private Long vote;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private ImgStatus imgStatus;

    @Builder
    public QuestionImg(Long id, Question question, String imgUrl, String description, Long vote, Boolean representFlag, ImgStatus imgStatus) {
        this.id = id;
        this.question = question;
        this.imgUrl = imgUrl;
        this.description = description;
        this.vote = vote;
        this.representFlag = representFlag;
        this.imgStatus = imgStatus;
    }
}
