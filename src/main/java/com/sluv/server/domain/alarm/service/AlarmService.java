package com.sluv.server.domain.alarm.service;

import static com.sluv.server.domain.alarm.enums.AlarmStatus.READ;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.dto.AlarmImages;
import com.sluv.server.domain.alarm.dto.AlarmResponse;
import com.sluv.server.domain.alarm.entity.Alarm;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.alarm.exception.AlarmAccessDeniedException;
import com.sluv.server.domain.alarm.exception.AlarmNotFoundException;
import com.sluv.server.domain.alarm.repository.AlarmRepository;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final ItemImgRepository itemImgRepository;
    private final QuestionImgRepository questionImgRepository;

    public void saveAlarm(User user, String title, String body, AlarmType alarmType, AlarmElement alarmElement) {
        alarmRepository.save(Alarm.toEntity(user, title, body, alarmType, alarmElement));
    }

    public void saveAllAlarm(List<User> users, String title, String body, AlarmType alarmType,
                             AlarmElement alarmElement) {

        List<Alarm> alarms = new ArrayList<>();
        for (User user : users) {
            alarms.add(Alarm.toEntity(user, title, body, alarmType, alarmElement));
        }
        alarmRepository.saveAll(alarms);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<AlarmResponse> getAlarmsByUserId(Long userId, Pageable pageable) {
        Page<Alarm> alarmPage = alarmRepository.findAllByUserId(userId, pageable);
        List<AlarmResponse> content = alarmPage.stream().map(alarm -> AlarmResponse.of(alarm, getAlarmImages(alarm)))
                .toList();
        return PaginationResDto.of(alarmPage, content);
    }

    private AlarmImages getAlarmImages(Alarm alarm) {
        String mainImageUrl = null;
        String useImageUrl = null;
        if (alarm.getAlarmType().equals(AlarmType.ITEM)) {
            mainImageUrl = itemImgRepository.findMainImg(alarm.getItem().getId()).getItemImgUrl();
            useImageUrl = alarm.getSender().getProfileImgUrl();
        }

        if (alarm.getAlarmType().equals(AlarmType.QUESTION)) {
            mainImageUrl = questionImgRepository.findByQuestionIdAndRepresentFlag(alarm.getQuestion().getId(), true)
                    .getImgUrl();
            useImageUrl = alarm.getSender().getProfileImgUrl();
        }

        if (alarm.getAlarmType().equals(AlarmType.COMMENT)) {
            mainImageUrl = questionImgRepository.findByQuestionIdAndRepresentFlag(alarm.getQuestion().getId(), true)
                    .getImgUrl();
            useImageUrl = alarm.getSender().getProfileImgUrl();
        }

        if (alarm.getAlarmType().equals(AlarmType.USER)) {
            useImageUrl = alarm.getSender().getProfileImgUrl();
        }

        if (alarm.getAlarmType().equals(AlarmType.REPORT)) {
            if (alarm.getItem() != null) {
                mainImageUrl = itemImgRepository.findMainImg(alarm.getItem().getId()).getItemImgUrl();
            }

            if (alarm.getQuestion() != null) {
                mainImageUrl = questionImgRepository.findByQuestionIdAndRepresentFlag(alarm.getQuestion().getId(), true)
                        .getImgUrl();
            }
        }

        if (alarm.getAlarmType().equals(AlarmType.EDIT)) {
            mainImageUrl = itemImgRepository.findMainImg(alarm.getItem().getId()).getItemImgUrl();
        }

        return AlarmImages.of(mainImageUrl, useImageUrl);
    }

    public void deleteAlarm(User user, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmNotFoundException::new);
        if (!alarm.getUser().getId().equals(user.getId())) {
            throw new AlarmAccessDeniedException();
        }
        alarmRepository.deleteById(alarmId);
    }

    public void deleteAllAlarm(User user) {
        alarmRepository.deleteAllByUserId(user.getId());
    }

    public void patchAlarmStatusToRead(User user, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmNotFoundException::new);
        if (Objects.equals(alarm.getUser().getId(), user.getId())) {
            alarm.changeStatus(READ);
        }
    }

}
