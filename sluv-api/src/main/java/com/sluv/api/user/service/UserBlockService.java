package com.sluv.api.user.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.user.dto.UserBlockDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserDomainService userDomainService;
    private final UserBlockDomainService userBlockDomainService;

    @Transactional
    public void postUserBlock(Long userId, Long targetId) {
        User user = userDomainService.findById(userId);
        User targetUser = userDomainService.findById(targetId);
        boolean blockStatus = userBlockDomainService.getBlockStatus(user, targetUser);

        if (blockStatus) {
            userBlockDomainService.deleteUserBlock(user, targetUser);
        } else {
            userBlockDomainService.postUserBlock(UserBlock.toEntity(user, targetUser));
        }

    }

    @Transactional(readOnly = true)
    public PaginationResponse<UserBlockDto> getUserBlock(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        Page<UserBlock> blockUserPage = userBlockDomainService.getUserBlockPage(user.getId(), pageable);

        List<UserBlockDto> content = blockUserPage.getContent().stream()
                .map(userBlock -> UserBlockDto.of(userBlock.getBlockedUser(), true))
                .toList();

        return PaginationResponse.create(blockUserPage, content);
    }
}
