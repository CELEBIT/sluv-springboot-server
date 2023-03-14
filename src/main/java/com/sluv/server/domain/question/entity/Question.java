package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "QTYPE")
//@SuperBuilder
public class Question{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 1001)
    private String content;

    @NotNull
    private Long searchNum;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private QuestionStatus questionStatus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "question")
    List<Comment> commentList;

    public Question(Long id, Long userId, String title, String content, Long searchNum) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.searchNum = searchNum;
    }
}
