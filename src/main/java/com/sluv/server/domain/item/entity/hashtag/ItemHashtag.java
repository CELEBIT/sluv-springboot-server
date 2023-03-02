package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "item_hashtag")
public class ItemHashtag extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_hashtag_id")
    private Long id;

    @NotNull
    private Long item_id;

    @NotNull
    private Long hashtag_id;

    @Builder
    public ItemHashtag(Long id, Long item_id, Long hashtag_id) {
        this.id = id;
        this.item_id = item_id;
        this.hashtag_id = hashtag_id;
    }
}
