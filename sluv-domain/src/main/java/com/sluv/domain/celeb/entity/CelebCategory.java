package com.sluv.domain.celeb.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.domain.common.entity.BaseEntity;
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
@Builder(toBuilder = true)
@JsonPropertyOrder({"id", "parent", "name", "created_at", "updated_at"})
@Table(name = "celeb_category")
public class CelebCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celeb_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CelebCategory parent;

    @NotNull
    @Size(max = 45)
    private String name;

}
