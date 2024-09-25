package com.sluv.domain.question.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.common.enums.ItemImgOrLinkStatus;
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "question_img")
public class QuestionImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @Size(max = 100)
    private String description;

    @NotNull
    @ColumnDefault("0")
    private Boolean representFlag;

    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "img_status", columnDefinition = "varchar(45) default 'ACTIVE'")
    private ItemImgOrLinkStatus itemImgOrLinkStatus = ItemImgOrLinkStatus.ACTIVE;

    public static QuestionImg toEntity(Question question, String imgUrl, String description, Boolean flag,
                                       Integer sortOrder) {
        return QuestionImg.builder()
                .question(question)
                .imgUrl(imgUrl)
                .description(description)
                .representFlag(flag)
                .sortOrder(sortOrder)
                .build();
    }
}
