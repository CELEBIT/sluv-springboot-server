package com.sluv.infra.discord;

import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.enums.UserWithdrawReason;
import com.sluv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscordWebHookService implements WebHookService {
    @Value("${discord.webhook.signup}")
    private String DISCORD_WEBHOOK_SIGNUP_URL;
    @Value("${discord.webhook.withdraw}")
    private String DISCORD_WEBHOOK_WITHDRAW_URL;
    @Value("${discord.webhook.new-brand}")
    private String DISCORD_WEBHOOK_NEW_BRAND_URL;
    @Value("${discord.webhook.new-celeb}")
    private String DISCORD_WEBHOOK_NEW_CELEB_URL;

    private final DiscordWebHookConnector discordWebHookConnector;
    private final UserRepository userRepository;

    @Override
    @Async("asyncThreadPoolExecutor")
    @Transactional
    public void sendSingupMessage(User user) {
        long userCount = userRepository.getNotDeleteUserCount();
        long activeUser = userRepository.countByUserStatus(UserStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime localTime = now.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        String message = new StringBuilder()
                .append("# 유저가 가입하였습니다.\n")
                .append("- 닉네임: ").append(user.getNickname()).append("\n")
                .append("- 이메일: ").append(user.getEmail()).append("\n")
                .append("- 연령대: ").append(user.getAgeRange()).append("\n")
                .append("- 성별: ").append(user.getGender()).append("\n")
                .append("- 가입 시간: ").append(date).append(" ").append(localTime).append("\n")
                .append("### 현재 __**").append(activeUser).append("명**__의 **활성화** 유저").append("\n")
                .append("### 현재 __**").append(userCount).append("명**__의 유저")
                .toString();

        WebHookMessage webHookMessage = new WebHookMessage(message);

        discordWebHookConnector.sendMessageForDiscord(webHookMessage, DISCORD_WEBHOOK_SIGNUP_URL);

    }

    @Override
    @Async("asyncThreadPoolExecutor")
    @Transactional
    public void sendWithdrawMessage(User user, UserWithdrawReason reason, String content) {
        long userCount = userRepository.getNotDeleteUserCount() - 1;
        long activeUser = userRepository.countByUserStatus(UserStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime localTime = now.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        String message = new StringBuilder()
                .append("# 유저가 탈퇴하였습니다.\n")
                .append("- 닉네임: ").append(user.getNickname()).append("\n")
                .append("- 이메일: ").append(user.getEmail()).append("\n")
                .append("- 연령대: ").append(user.getAgeRange()).append("\n")
                .append("- 성별: ").append(user.getGender()).append("\n")
                .append("- 탈퇴 시간: ").append(date).append(" ").append(localTime).append("\n")
                .append("- 탈퇴 사유: ").append(reason).append(" -> ").append(content).append("\n")
                .append("### 현재 __**").append(activeUser).append("명**__의 **활성화** 유저").append("\n")
                .append("### 현재 __**").append(userCount).append("명**__의 유저")
                .toString();

        WebHookMessage webHookMessage = new WebHookMessage(message);

        discordWebHookConnector.sendMessageForDiscord(webHookMessage, DISCORD_WEBHOOK_WITHDRAW_URL);

    }

    @Override
    @Async("asyncThreadPoolExecutor")
    @Transactional
    public void sendCreateNewBrandMessage(NewBrand newBrand) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime localTime = now.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        String message = new StringBuilder()
                .append("# NewBrand가 등록되었습니다.\n")
                .append("- 이름: ").append(newBrand.getBrandName()).append("\n")
                .append("- 등록 시간: ").append(date).append(" ").append(localTime).append("\n")
                .toString();

        WebHookMessage webHookMessage = new WebHookMessage(message);

        discordWebHookConnector.sendMessageForDiscord(webHookMessage, DISCORD_WEBHOOK_NEW_BRAND_URL);
    }

    @Override
    @Async("asyncThreadPoolExecutor")
    @Transactional
    public void sendCreateNewCelebMessage(NewCeleb newCeleb) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime localTime = now.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        String message = new StringBuilder()
                .append("# NewCeleb이 등록되었습니다.\n")
                .append("- 이름: ").append(newCeleb.getCelebName()).append("\n")
                .append("- 등록 시간: ").append(date).append(" ").append(localTime).append("\n")
                .toString();

        WebHookMessage webHookMessage = new WebHookMessage(message);

        discordWebHookConnector.sendMessageForDiscord(webHookMessage, DISCORD_WEBHOOK_NEW_CELEB_URL);
    }
}
