package com.sluv.server.domain.alarm.dto;

import com.sluv.server.domain.alarm.entity.Alarm;
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

    private Long itemId;
    private Long questionId;
    private Long commentId;
    private Long followerId;


    private LocalDateTime cratedAt;

    public static AlarmResponse of(Alarm alarm) {

        Long itemId = alarm.getItem() == null ? null : alarm.getItem().getId();
        Long questionId = alarm.getQuestion() == null ? null : alarm.getQuestion().getId();
        Long commentId = alarm.getComment() == null ? null : alarm.getComment().getId();
        Long followerId = alarm.getFollower() == null ? null : alarm.getFollower().getId();

        return AlarmResponse.builder()
                .alarmId(alarm.getId())
                .title(alarm.getTitle())
                .body(alarm.getBody())
                .itemId(itemId)
                .questionId(questionId)
                .commentId(commentId)
                .followerId(followerId)
                .cratedAt(alarm.getCreatedAt())
                .build();
    }

}