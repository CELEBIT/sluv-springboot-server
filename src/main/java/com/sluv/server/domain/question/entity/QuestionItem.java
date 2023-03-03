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
@Table(name = "question_item")
public class QuestionItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_item_id")
    private Long id;

    @NotNull
    private Long questionId;

    @NotNull
    private Long itemId;

    @Size(max = 100)
    private String description;

    private Long vote;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Builder
    public QuestionItem(Long id, Long questionId, Long itemId, String description, Long vote, Boolean representFlag) {
        this.id = id;
        this.questionId = questionId;
        this.itemId = itemId;
        this.description = description;
        this.vote = vote;
        this.representFlag = representFlag;
    }
}
