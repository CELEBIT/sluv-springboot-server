package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.NewBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewBrandRepository extends JpaRepository<NewBrand, Long> {
    Optional<NewBrand> findByBrandName(String newBrandName);
}
