package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.global.common.entity.BaseEntity;
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

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @Size(max = 100)
    private String description;

    private Long vote;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    @Builder
    public QuestionItem(Long id, Question question, Item item, String description, Long vote) {
        this.id = id;
        this.question = question;
        this.item = item;
        this.description = description;
        this.vote = vote;
    }
}
