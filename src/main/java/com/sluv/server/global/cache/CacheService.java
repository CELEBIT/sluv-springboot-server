package com.sluv.server.global.cache;

import com.sluv.server.domain.item.dto.ItemDetailFixData;
import org.springframework.stereotype.Service;

@Service
public interface CacheService {
    void visitMember(Long memberId);

    Long getVisitantCount();

    void clearVisitantCount();

    void saveItemDetailFixData(Long itemId, ItemDetailFixData itemDetailFixData);

    ItemDetailFixData findItemDetailFixDataByItemId(Long itemId);

    void deleteItemDetailFixDataByItemId(Long itemId);

    void saveUserViewItemId(Long userId, Long itemId);

    boolean existUserViewItemId(Long userId, Long itemId);

    void saveUserViewQuestionId(Long userId, Long questionId);

    boolean existUserViewQuestionId(Long userId, Long questionId);

}
