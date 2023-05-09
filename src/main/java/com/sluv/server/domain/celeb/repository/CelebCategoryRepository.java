package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.CelebCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CelebCategoryRepository extends JpaRepository<CelebCategory, Long> {
    List<CelebCategory> findAllByParentIdIsNull();
}
