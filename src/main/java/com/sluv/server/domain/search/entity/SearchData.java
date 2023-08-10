package com.sluv.server.domain.search.entity;

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
@Table(name = "search_data")
public class SearchData extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_data_id")
    private Long id;

    @NotNull
    @Size(max = 255)
    private String searchWord;

    public static SearchData of(String searchWord) {
        return SearchData.builder()
                .searchWord(searchWord)
                .build();
    }
}
