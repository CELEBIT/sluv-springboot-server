package com.sluv.admin.user.service;

import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.user.dto.HotUserResDto;
import com.sluv.admin.user.dto.UserAccountCountResDto;
import com.sluv.admin.user.dto.UserAdminInfoDto;
import com.sluv.admin.user.dto.UserCountByCategoryResDto;
import com.sluv.domain.user.dto.UserReportStackDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public PaginationResponse<UserAdminInfoDto> getAllUserInfo(Pageable pageable) {
        Page<UserReportStackDto> stackDtoPage = userDomainService.getAllUserInfo(pageable);
        List<UserAdminInfoDto> content = stackDtoPage.getContent().stream()
                .map(UserAdminInfoDto::from)
                .toList();

        return PaginationResponse.create(stackDtoPage, content);
    }

    @Transactional
    public void updateUserStatus(Long userId, UserStatus userStatus) {
        User user = userDomainService.findById(userId);
        user.changeUserStatus(userStatus);
    }

    @Transactional(readOnly = true)
    public UserCountByCategoryResDto getUserCountByGender() {
        List<User> allUser = userDomainService.findAll();
        HashMap<UserGender, Long> countByGender = allUser.stream()
                .collect(Collectors.groupingBy(User::getGender, HashMap::new, Collectors.counting()));
        return UserCountByCategoryResDto.of(countByGender, allUser.stream().count());
    }

    @Transactional(readOnly = true)
    public UserCountByCategoryResDto getUserCountByAge() {
        List<User> allUser = userDomainService.findAll();
        HashMap<UserAge, Long> countByGender = allUser.stream()
                .collect(Collectors.groupingBy(User::getAgeRange, HashMap::new, Collectors.counting()));

        return UserCountByCategoryResDto.of(countByGender, allUser.stream().count());
    }

    @Transactional(readOnly = true)
    public UserAccountCountResDto getUserAccountCount() {
        LocalDateTime now = LocalDateTime.now();
        List<User> allUser = userDomainService.findAll();
        long recentMonthCount = getRecentMonthCount(now, allUser);
        List<Long> countGraph = getCountGraph(now, allUser);
        return UserAccountCountResDto.of(allUser.stream().count(), recentMonthCount, countGraph);
    }

    private static List<Long> getCountGraph(LocalDateTime now, List<User> allUser) {
        List<Long> countGraph = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            LocalDateTime startWeek = now.minusWeeks(i);
            LocalDateTime endWeek = now.minusWeeks(i - 1);
            long count = allUser.stream()
                    .filter(user -> user.getCreatedAt().isAfter(startWeek) && user.getCreatedAt().isBefore(endWeek))
                    .count();
            countGraph.add(count);
        }
        return countGraph;
    }

    private static long getRecentMonthCount(LocalDateTime now, List<User> allUser) {
        return allUser.stream()
                .filter(user -> user.getCreatedAt().isAfter(now.minusMonths(1)))
                .count();
    }

    @Transactional(readOnly = true)
    public List<HotUserResDto> getTop3HotUser() {
        return userDomainService.getTop3HotUser().stream()
                .map(HotUserResDto::from)
                .toList();
    }

}
