package com.sluv.server.domain.search.entity;

import com.sluv.server.domain.search.enums.SearchRankStatus;
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
@Table(name = "search_rank")
public class SearchRank extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_rank_id")
    private Long id;
    @NotNull
    @Size(max = 255)
    private String searchWord;
    @NotNull
    private Long searchCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'TODAY'")
    private SearchRankStatus searchRankStatus;

    public static SearchRank of(Long searchCount, String searchWord) {
        return SearchRank.builder()
                .searchCount(searchCount)
                .searchWord(searchWord)
                .build();
    }
}
