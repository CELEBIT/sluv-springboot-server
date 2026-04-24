package com.sluv.domain.featureflag.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "feature_flag")
public class FeatureFlag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_flag_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false, unique = true)
    private FeatureFlagKey flagKey;

    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    @Size(max = 255)
    private String description;
}
