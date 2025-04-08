package com.sluv.domain.user.service;

import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.dto.UserReportStackDto;
import com.sluv.domain.user.dto.UserWithFollowerCountDto;
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
        if (userId == null) {
            return null;
        }
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

    public List<User> getHotSluver(Long celebId, List<Long> blockUserIds) {
        return userRepository.getHotSluver(celebId, blockUserIds);
    }

    public Page<User> getSearchUser(List<Long> searchUserIds, Pageable pageable) {
        return userRepository.getSearchUser(searchUserIds, pageable);
    }

    public User findBySnsWithEmailOrNull(String email, SnsType snsType) {
        return userRepository.findBySnsWithEmailOrNull(email, snsType).orElse(null);
    }

    public Page<UserReportStackDto> getAllUserInfo(Pageable pageable) {
        return userRepository.getAllUserInfo(pageable);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserWithFollowerCountDto> getTop3HotUser() {
        return userRepository.getTop3HotUser();
    }
}
