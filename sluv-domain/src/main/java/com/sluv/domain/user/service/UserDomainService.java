package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotFoundException;
import com.sluv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllFollower(Long targetId, Pageable pageable) {
        return userRepository.getAllFollower(targetId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllFollowing(Long userId, Pageable pageable) {
        return userRepository.getAllFollowing(userId, pageable);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByIdOrNull(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Boolean existsByNickname(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

    @Transactional(readOnly = true)
    public List<User> getHotSluver(Long celebId) {
        return userRepository.getHotSluver(celebId);
    }

    @Transactional(readOnly = true)
    public Page<User> getSearchUser(List<Long> searchUserIds, Pageable pageable) {
        return userRepository.getSearchUser(searchUserIds, pageable);
    }

    @Transactional(readOnly = true)
    public User findByEmailOrNull(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
