package com.sluv.domain.alarm.dto;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
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
