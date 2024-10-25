package com.sluv.domain.brand.repository;

import com.sluv.domain.brand.entity.NewBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewBrandRepository extends JpaRepository<NewBrand, Long> {
    Optional<NewBrand> findByBrandName(String newBrandName);

    List<NewBrand> findAllByOrderByCreatedAtDesc();

}
