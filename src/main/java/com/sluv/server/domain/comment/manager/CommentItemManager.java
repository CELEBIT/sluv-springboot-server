package com.sluv.server.domain.comment.manager;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.entity.CommentItem;
import com.sluv.server.domain.comment.repository.CommentItemRepository;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentItemManager {
    private final CommentItemRepository commentItemRepository;
    private final ItemRepository itemRepository;

    /**
     * 댓글 아이템 등록
     */
    public void saveCommentItem(CommentPostReqDto dto, Comment comment) {
        if (dto.getItemList() != null) {
            // 초기화
            commentItemRepository.deleteAllByCommentId(comment.getId());
            if (dto.getItemList() != null) {
                // dto로 부터 새로운 CommentItem 생성
                List<CommentItem> itemList = dto.getItemList().stream().map(itemReqDto -> {
                            Item item = itemRepository.findById(itemReqDto.getItemId()).orElseThrow(ItemNotFoundException::new);
                            return CommentItem.toEntity(comment, item, itemReqDto);
                        }
                ).toList();

                // 저장
                commentItemRepository.saveAll(itemList);
            }
        }
    }


}
