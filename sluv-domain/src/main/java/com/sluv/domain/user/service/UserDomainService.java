package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotFoundException;
import com.sluv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public Page<User> getAllFollower(Long targetId, Pageable pageable) {
        return userRepository.getAllFollower(targetId, pageable);
    }

    public Page<User> getAllFollowing(Long userId, Pageable pageable) {
        return userRepository.getAllFollowing(userId, pageable);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByIdOrNull(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    public Boolean existsByNickname(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

    public List<User> getHotSluver(Long celebId) {
        return userRepository.getHotSluver(celebId);
    }

    public Page<User> getSearchUser(List<Long> searchUserIds, Pageable pageable) {
        return userRepository.getSearchUser(searchUserIds, pageable);
    }

    public User findByEmailOrNull(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
