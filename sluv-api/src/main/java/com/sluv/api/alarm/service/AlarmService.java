package com.sluv.api.alarm.service;

import com.sluv.api.alarm.dto.AlarmCheckResponse;
import com.sluv.api.alarm.dto.AlarmResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.alarm.dto.AlarmImages;
import com.sluv.domain.alarm.entity.Alarm;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.service.QuestionImgDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmDomainService alarmDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final QuestionImgDomainService questionImgDomainService;


    @Transactional(readOnly = true)
    public PaginationResponse<AlarmResponse> getAlarmsByUserId(Long userId, Pageable pageable) {
        Page<Alarm> alarmPage = alarmDomainService.findAllByUserId(userId, pageable);
        List<AlarmResponse> content = alarmPage.stream()
                .map(alarm -> AlarmResponse.of(alarm, getAlarmImages(alarm)))
                .toList();
        return PaginationResponse.of(alarmPage, content);
    }

    private AlarmImages getAlarmImages(Alarm alarm) {
        List<QuestionImgSimpleDto> images = new ArrayList<>();
        String useImageUrl = null;

        if (alarm.getSender() != null) {
            useImageUrl = alarm.getSender().getProfileImgUrl();
        }

        if (alarm.getItem() != null) {
            ItemImg mainImg = itemImgDomainService.findMainImg(alarm.getItem().getId());
            images.add(QuestionImgSimpleDto.of(mainImg));
        }

        if (alarm.getQuestion() != null) {
            images = questionImgDomainService.findAllByQuestionId(alarm.getQuestion().getId())
                    .stream()
                    .map(QuestionImgSimpleDto::of)
                    .toList();
        }

        return AlarmImages.of(images, useImageUrl);
    }

    @Transactional
    public void patchAlarmStatusToRead(Long userId, Long alarmId) {
        alarmDomainService.patchAlarmStatusToRead(userId, alarmId);
    }

    @Transactional
    public void deleteAlarm(Long userId, Long alarmId) {
        alarmDomainService.deleteAlarm(userId, alarmId);
    }

    @Transactional
    public void deleteAllAlarm(Long useId) {
        alarmDomainService.deleteAllAlarm(useId);
    }

    @Transactional(readOnly = true)
    public AlarmCheckResponse checkAlarmAllRead(Long userId) {
        Boolean isAllRead = alarmDomainService.checkAllRead(userId);
        return AlarmCheckResponse.of(isAllRead);
    }

}
