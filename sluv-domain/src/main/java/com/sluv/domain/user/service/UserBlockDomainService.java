package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockDomainService {

    private final UserBlockRepository userBlockRepository;

    public boolean getBlockStatus(User user, User targetUser) {
        return userBlockRepository.getBlockStatus(user, targetUser);
    }

    public void deleteUserBlock(User user, User targetUser) {
        userBlockRepository.deleteUserBlock(user, targetUser);
    }

    public void postUserBlock(UserBlock userBlock) {
        userBlockRepository.save(userBlock);
    }

    public Page<UserBlock> getUserBlockPage(Long userId, Pageable pageable) {
        return userBlockRepository.getUserBlockPage(userId, pageable);
    }

    public List<UserBlock> getAllBlockedUser(Long userId) {
        return userBlockRepository.getAllBlockUser(userId);
    }
}
