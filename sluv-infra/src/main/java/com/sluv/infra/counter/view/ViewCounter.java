package com.sluv.infra.counter.view;

import org.springframework.stereotype.Component;

@Component
public interface ViewCounter {
    void saveUserViewItemId(Long userId, Long itemId);

    boolean existUserViewItemId(Long userId, Long itemId);

    void saveUserViewQuestionId(Long userId, Long questionId);

    boolean existUserViewQuestionId(Long userId, Long questionId);
}
