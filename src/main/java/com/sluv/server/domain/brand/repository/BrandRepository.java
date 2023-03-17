package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {
}
