package com.sluv.server.domain.alarm.entity;

import static com.sluv.server.domain.alarm.enums.AlarmStatus.ACTIVE;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.enums.AlarmStatus;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String body;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    private AlarmStatus alarmStatus;

    public static Alarm toEntity(User user, String title, String body, AlarmType alarmType, AlarmElement alarmElement) {
        return Alarm.builder()
                .user(user)
                .title(title)
                .body(body)
                .alarmType(alarmType)
                .item(alarmElement.getItem())
                .question(alarmElement.getQuestion())
                .comment(alarmElement.getComment())
                .sender(alarmElement.getUser())
                .alarmStatus(ACTIVE)
                .build();
    }

    public void changeStatus(AlarmStatus alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
