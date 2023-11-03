package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.dto.QuestionImgReqDto;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static QuestionImg toEntity(Question question, QuestionImgReqDto reqDto) {
        return QuestionImg.builder()
                .question(question)
                .imgUrl(reqDto.getImgUrl())
                .description(reqDto.getDescription())
                .representFlag(reqDto.getRepresentFlag())
                .sortOrder(reqDto.getSortOrder())
                .build();
    }
}
