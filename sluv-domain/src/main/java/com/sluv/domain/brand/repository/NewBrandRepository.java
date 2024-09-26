package com.sluv.domain.brand.repository;

import com.sluv.domain.brand.entity.NewBrand;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewBrandRepository extends JpaRepository<NewBrand, Long> {
    Optional<NewBrand> findByBrandName(String newBrandName);
}
