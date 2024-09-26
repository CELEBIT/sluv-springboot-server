package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.CelebCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelebCategoryRepository extends JpaRepository<CelebCategory, Long> {
    List<CelebCategory> findAllByParentIdIsNull();
}
