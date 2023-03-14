package com.sluv.server.domain.celeb.entity;

import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;
import com.sluv.server.domain.comment.entity.CommentReport;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "new_celeb")
public class NewCeleb extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_celeb_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private NewCelebStatus newCelebStatus;

    @Builder
    public NewCeleb(Long id, String celebName, NewCelebStatus newCelebStatus) {
        this.id = id;
        this.celebName = celebName;
        this.newCelebStatus = newCelebStatus;
    }
}
