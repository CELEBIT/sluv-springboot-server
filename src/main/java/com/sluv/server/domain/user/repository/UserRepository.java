package com.sluv.server.domain.user.repository;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.impl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Boolean existsByNickname(String nickName);
}