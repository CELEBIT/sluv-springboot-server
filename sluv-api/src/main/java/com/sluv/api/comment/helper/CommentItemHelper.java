package com.sluv.api.comment.helper;

import com.sluv.api.comment.dto.request.CommentPostRequest;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentItem;
import com.sluv.domain.comment.repository.CommentItemRepository;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.exception.ItemNotFoundException;
import com.sluv.domain.item.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentItemHelper {
    private final CommentItemRepository commentItemRepository;
    private final ItemRepository itemRepository;

    /**
     * 댓글 아이템 등록
     */
    public void saveCommentItem(CommentPostRequest dto, Comment comment) {
        if (dto.getItemList() != null) {
            // 초기화
            commentItemRepository.deleteAllByCommentId(comment.getId());
            if (dto.getItemList() != null) {
                // dto로 부터 새로운 CommentItem 생성
                List<CommentItem> itemList = dto.getItemList().stream().map(itemReqDto -> {
                            Item item = itemRepository.findById(itemReqDto.getItemId()).orElseThrow(
                                    ItemNotFoundException::new);
                            return CommentItem.toEntity(comment, item, itemReqDto.getSortOrder());
                        }
                ).toList();

                // 저장
                commentItemRepository.saveAll(itemList);
            }
        }
    }


}
