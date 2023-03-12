package com.sluv.server.domain.search.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recent_search")
public class RecentSearch extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_search_id")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 255)
    private String searchWord;

    @Builder
    public RecentSearch(Long id, Long userId, String searchWord) {
        this.id = id;
        this.userId = userId;
        this.searchWord = searchWord;
    }
}
