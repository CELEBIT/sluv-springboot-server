package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.repository.impl.UserRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Boolean existsByNickname(String nickName);

    long countByUserStatus(UserStatus userStatus);
}