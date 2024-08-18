package com.sluv.server.domain.question.scheduler;

import com.sluv.server.domain.alarm.service.QuestionAlarmService;
import com.sluv.server.domain.question.entity.DailyHotQuestion;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.repository.DailyHotQuestionRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class QuestionScheduler {

    private final DailyHotQuestionRepository dailyHotQuestionRepository;
    private final QuestionRepository questionRepository;
    private final QuestionAlarmService questionAlarmService;

    private static final int VOTE_END_CHECK_PERIOD = 60000; // 1분

    /**
     * 일간 HOT Question
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDailyHotQuestion() {
        log.info("DailyHotQuestion Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old DailyHotQuestion Data");
        dailyHotQuestionRepository.deleteAll();

        log.info("Get DailyHotQuestion. Time: {}", Calendar.getInstance().getTime());
        List<Question> newDailyHotQuestion = questionRepository.updateDailyHotQuestion();

        log.info("Save DailyHotQuestion. Time: {}", Calendar.getInstance().getTime());

        newDailyHotQuestion.forEach(question ->
                dailyHotQuestionRepository.save(
                        DailyHotQuestion.toEntity(question)
                )
        );
    }

    /**
     * 투표 종료 확인
     */
    @Scheduled(fixedRate = VOTE_END_CHECK_PERIOD) // 초 분 시 일 월 요일
    public void checkQuestionVoteEnd() {
        log.info("QuestionVoteEnd Check Time: {}", Calendar.getInstance().getTime());
        List<QuestionBuy> endTimeBetweenNow = questionRepository.getEndTimeBetweenNow(VOTE_END_CHECK_PERIOD);
        endTimeBetweenNow.forEach(questionBuy -> System.out.println("dd: " + questionBuy.getId()));
        endTimeBetweenNow.forEach(questionBuy -> questionAlarmService.sendAlarmAboutQuestionVote(questionBuy.getId()));

    }

}
