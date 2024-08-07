package com.sluv.server.global.ai;

import com.sluv.server.domain.alarm.service.CommentAlarmService;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiModelService {
    private final AiModelRepository aiModelRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    private final CommentAlarmService commentAlarmService;

    @Async(value = "asyncThreadPoolExecutor")
    public void censorComment(Comment comment) {
        boolean isMalicious = aiModelRepository.isMaliciousComment(comment.getContent());

        if (isMalicious) {
            comment.changeStatus(CommentStatus.BLOCKED);
            commentRepository.save(comment);
            commentAlarmService.sendAlarmAboutReportByAI(comment.getId(), null);
        }
    }

    @Async(value = "asyncThreadPoolExecutor")
    public void getItemColor(Item item) {
        ItemImg mainImg = itemImgRepository.findMainImg(item.getId());

        String color = aiModelRepository.getItemColor(mainImg.getItemImgUrl());

        item.changeColor(color.replace("\"", ""));
        itemRepository.save(item);
    }
}
