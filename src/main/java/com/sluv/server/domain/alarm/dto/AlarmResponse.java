package com.sluv.server.domain.alarm.dto;

import com.sluv.server.domain.alarm.entity.Alarm;
import com.sluv.server.domain.alarm.enums.AlarmStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponse {
    private Long alarmId;
    private String title;
    private String body;
    private String type;

    private Long itemId;
    private Long questionId;
    private Long commentId;
    private Long followerId;

    private String mainImageUrl;
    private String userImageUrl;


    private LocalDateTime cratedAt;
    private AlarmStatus alarmStatus;

    public static AlarmResponse of(Alarm alarm, AlarmImages alarmImages) {

        Long itemId = alarm.getItem() == null ? null : alarm.getItem().getId();
        Long questionId = alarm.getQuestion() == null ? null : alarm.getQuestion().getId();
        Long commentId = alarm.getComment() == null ? null : alarm.getComment().getId();
        Long followerId = alarm.getSender() == null ? null : alarm.getSender().getId();

        return AlarmResponse.builder()
                .alarmId(alarm.getId())
                .title(alarm.getTitle())
                .body(alarm.getBody())
                .type(alarm.getAlarmType().getName())
                .itemId(itemId)
                .questionId(questionId)
                .commentId(commentId)
                .followerId(followerId)
                .mainImageUrl(alarmImages.getMainImageUrl())
                .userImageUrl(alarmImages.getUserImageUrl())
                .cratedAt(alarm.getCreatedAt())
                .alarmStatus(alarm.getAlarmStatus())
                .build();
    }

}
