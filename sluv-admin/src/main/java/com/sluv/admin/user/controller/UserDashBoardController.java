package com.sluv.admin.user.controller;

import com.sluv.admin.common.response.SuccessDataResponse;
import com.sluv.admin.user.dto.HotUserResDto;
import com.sluv.admin.user.dto.UserAccountCountResDto;
import com.sluv.admin.user.dto.UserCountByCategoryResDto;
import com.sluv.admin.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/backoffice/user/dashBoard")
public class UserDashBoardController {

    private final UserService userService;

    @Operation(
            summary = "대시보드 - 유저의 성별 분포 조회",
            description = "대시보드에서 유저의 성별 분포를 출력한다"
    )
    @GetMapping("/gender")
    public ResponseEntity<SuccessDataResponse<UserCountByCategoryResDto>> getUserCountByGender() {
        UserCountByCategoryResDto response = userService.getUserCountByGender();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }


    @Operation(
            summary = "대시보드 - 유저의 연령대 분포 조회",
            description = "대시보드에서 유저의 연령대 분포를 출력한다"
    )
    @GetMapping("/age")
    public ResponseEntity<SuccessDataResponse<UserCountByCategoryResDto>> getUserCountByAge() {
        UserCountByCategoryResDto response = userService.getUserCountByAge();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "대시보드 - 가입자 수 조회 기능",
            description = "대시보드에서 가입자 수 조회 기능\n"
                    + "1. 전체 가입자 수\n"
                    + "2. 지난 1달간 증가 수\n"
                    + "3. 지난 10주간 그래프"

    )
    @GetMapping("/accountCount")
    public ResponseEntity<SuccessDataResponse<UserAccountCountResDto>> getUserAccountCount() {
        UserAccountCountResDto response = userService.getUserAccountCount();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "대시보드 - 인기 유저 Top3 조회 기능",
            description = "대시보드에서 인기 유저 Top3 조회 기능\n"
                    + "팔로워 수를 기준으로 내림차순"

    )
    @GetMapping("/hotUser")
    public ResponseEntity<SuccessDataResponse<List<HotUserResDto>>> getTop3HotUser() {
        List<HotUserResDto> response = userService.getTop3HotUser();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
