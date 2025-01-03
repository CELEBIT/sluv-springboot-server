package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.CelebCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CelebCategoryRepository extends JpaRepository<CelebCategory, Long> {
    List<CelebCategory> findAllByParentIdIsNull();

    List<CelebCategory> findAllByParentIdIsNotNull();
}
