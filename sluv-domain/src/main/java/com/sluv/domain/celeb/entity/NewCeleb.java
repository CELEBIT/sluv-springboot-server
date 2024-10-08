package com.sluv.domain.celeb.entity;

import com.sluv.domain.celeb.enums.NewCelebStatus;
import com.sluv.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "new_celeb")
public class NewCeleb extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_celeb_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private NewCelebStatus newCelebStatus;

    public static NewCeleb toEntity(String name) {
        return NewCeleb.builder()
                .celebName(name)
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build();
    }
}
