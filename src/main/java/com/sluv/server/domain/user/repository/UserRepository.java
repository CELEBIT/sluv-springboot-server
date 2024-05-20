package com.sluv.server.domain.user.repository;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.repository.impl.UserRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Boolean existsByNickname(String nickName);

    long countByUserStatus(UserStatus userStatus);
}