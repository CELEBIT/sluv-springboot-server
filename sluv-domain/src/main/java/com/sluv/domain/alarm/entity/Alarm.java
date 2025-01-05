package com.sluv.domain.alarm.entity;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmStatus;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_edit_req_id")
    private ItemEditReq itemEditReq;

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
                .itemEditReq(alarmElement.getItemEditReq())
                .alarmStatus(AlarmStatus.ACTIVE)
                .build();
    }

    public void changeStatus(AlarmStatus alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
