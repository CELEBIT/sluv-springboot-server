package com.sluv.server.domain.item.entity.hashtag;

import com.sluv.server.domain.item.entity.TempItem;
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
    private Hashtag hashtag;

    public static TempItemHashtag toEntity(TempItem tempItem, Hashtag hashtag) {
        return TempItemHashtag.builder()
                .tempItem(tempItem)
                .hashtag(hashtag)
                .build();
    }
}
