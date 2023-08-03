package com.sluv.server.domain.celeb.entity;

import com.sluv.server.domain.celeb.dto.NewCelebPostReqDto;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_celeb_id")
    private Long id;

    @NotNull
    @Size(max = 300)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private NewCelebStatus newCelebStatus;

    public static NewCeleb toEntity(NewCelebPostReqDto dto){
        return NewCeleb.builder()
                .celebName(dto.getNewCelebName())
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build();
    }
}
