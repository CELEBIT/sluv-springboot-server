package com.sluv.infra.cache;

import org.springframework.stereotype.Service;

@Service
public interface CacheService<T> {
    void visitMember(Long memberId);

    Long getVisitantCount();

    void clearVisitantCount();

    //
    void saveItemDetailFixData(Long itemId, T itemDetailFixData);

    T findItemDetailFixDataByItemId(Long itemId);
//    void saveItemDetailFixData(Long itemId, T itemDetailFixData);
//
//    <T> findItemDetailFixDataByItemId(Long itemId);

    void deleteItemDetailFixDataByItemId(Long itemId);

    void saveUserViewItemId(Long userId, Long itemId);

    boolean existUserViewItemId(Long userId, Long itemId);

    void saveUserViewQuestionId(Long userId, Long questionId);

    boolean existUserViewQuestionId(Long userId, Long questionId);

}
