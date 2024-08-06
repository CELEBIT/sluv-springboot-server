package com.sluv.server.domain.alarm.dto;

import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmElement {

    private Item item;
    private Question question;
    private Comment comment;
    private User user;

    public static AlarmElement of(Item item, Question question, Comment comment, User user) {
        return AlarmElement.builder()
                .item(item)
                .question(question)
                .comment(comment)
                .user(user)
                .build();
    }
}
