package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "hashtag")
public class Hashtag extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @NotNull
    @Size(max = 45)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'")
    private HashtagStatus hashtagStatus;

    @Builder
    public Hashtag(Long id, String content, HashtagStatus hashtagStatus) {
        this.id = id;
        this.content = content;
        this.hashtagStatus = hashtagStatus;
    }
}
