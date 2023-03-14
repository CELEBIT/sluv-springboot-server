package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.domain.item.entity.Item;
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

    @ManyToOne
    @JoinColumn(name = "item_item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "hashtag_hashtag_id")
    @NotNull
    private Hashtag hashtag;

    @Builder
    public ItemHashtag(Long id, Item item, Hashtag hashtag) {
        this.id = id;
        this.item = item;
        this.hashtag = hashtag;
    }
}
