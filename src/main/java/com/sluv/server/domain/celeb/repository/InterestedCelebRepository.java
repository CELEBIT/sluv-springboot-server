package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestedCelebRepository extends JpaRepository<InterestedCeleb, Long>, CelebRepositoryCustom {
    List<InterestedCeleb> findAllByUser(User user);

}