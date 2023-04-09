package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "temp_item_hashtag")
public class TempItemHashtag extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_item_hashtag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "temp_item_id")
    @NotNull
    private TempItem tempItem;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    @NotNull
    private Hashtag hashtag;

    @Builder
    public TempItemHashtag(Long id, TempItem tempItem, Hashtag hashtag) {
        this.id = id;
        this.tempItem = tempItem;
        this.hashtag = hashtag;
    }
}
