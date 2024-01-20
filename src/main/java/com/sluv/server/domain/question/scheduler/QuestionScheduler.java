package com.sluv.server.domain.question.scheduler;

import com.sluv.server.domain.question.entity.DailyHotQuestion;
import com.sluv.server.domain.question.entity.Question;
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
}
