package com.sluv.domain.featureflag.repository;

import com.sluv.domain.config.TestConfig;
import com.sluv.domain.featureflag.entity.FeatureFlag;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class FeatureFlagRepositoryTest {

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @AfterEach
    void clean() {
        featureFlagRepository.deleteAll();
    }

    @Test
    @DisplayName("flagKey로 Feature Flag를 조회한다.")
    void findByFlagKeyTest() {
        // given
        FeatureFlag featureFlag = FeatureFlag.builder()
                .flagKey(FeatureFlagKey.MODERATION_JOB_CREATION)
                .enabled(true)
                .description("게시글 업로드 시 검수 작업 생성 여부")
                .build();
        featureFlagRepository.save(featureFlag);

        // when
        Optional<FeatureFlag> result = featureFlagRepository.findByFlagKey(
                FeatureFlagKey.MODERATION_JOB_CREATION
        );

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getFlagKey()).isEqualTo(FeatureFlagKey.MODERATION_JOB_CREATION);
        assertThat(result.get().isEnabled()).isTrue();
    }

    @Test
    @DisplayName("비활성화된 Feature Flag를 저장하고 조회한다.")
    void findDisabledFeatureFlagTest() {
        // given
        FeatureFlag featureFlag = FeatureFlag.builder()
                .flagKey(FeatureFlagKey.MODERATION_AUTO_APPLY_RESULT)
                .enabled(false)
                .description("검수 결과 자동 반영 여부")
                .build();
        featureFlagRepository.save(featureFlag);

        // when
        Optional<FeatureFlag> result = featureFlagRepository.findByFlagKey(
                FeatureFlagKey.MODERATION_AUTO_APPLY_RESULT
        );

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getFlagKey()).isEqualTo(FeatureFlagKey.MODERATION_AUTO_APPLY_RESULT);
        assertThat(result.get().isEnabled()).isFalse();
    }
}
