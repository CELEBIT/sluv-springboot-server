package com.sluv.domain.featureflag.repository;

import com.sluv.domain.featureflag.entity.FeatureFlag;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    Optional<FeatureFlag> findByFlagKey(FeatureFlagKey flagKey);
}
