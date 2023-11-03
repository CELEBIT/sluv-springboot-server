package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "item_hashtag")
public class ItemHashtag extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    @NotNull
    private Hashtag hashtag;

    public static ItemHashtag toEntity(Item item, Hashtag hashtag) {
        return ItemHashtag.builder()
                .item(item)
                .hashtag(hashtag)
                .build();
    }
}
