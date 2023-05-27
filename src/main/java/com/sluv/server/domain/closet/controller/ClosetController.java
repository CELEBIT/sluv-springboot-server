package com.sluv.server.domain.closet.controller;

import com.sluv.server.domain.closet.dto.ClosetPostReqDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/closet")
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;

    @Operation(
            summary = "*옷장 생성",
            description = "사용자의 옷장을 생성" +
                    "\n User Id Token 필요"
    )
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postCloset(@AuthenticationPrincipal User user, @RequestBody ClosetPostReqDto dto){
        closetService.postCloset(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
