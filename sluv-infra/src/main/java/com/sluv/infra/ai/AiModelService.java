package com.sluv.infra.ai;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.comment.repository.CommentRepository;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.item.repository.ItemRepository;
import com.sluv.infra.alarm.service.CommentAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiModelService {
    private final AiModelRepository aiModelRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final CommentAlarmService commentAlarmService;


    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public void censorComment(Comment comment) {
        boolean isMalicious = aiModelRepository.isMaliciousComment(comment.getContent());

        if (isMalicious) {
            comment.changeStatus(CommentStatus.BLOCKED);
            commentRepository.save(comment);
            commentAlarmService.sendAlarmAboutReportByAI(comment.getId(), null);
        }
    }

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public void getItemColor(Item item) {
        ItemImg mainImg = itemImgRepository.findMainImg(item.getId());

        String color = aiModelRepository.getItemColor(mainImg.getItemImgUrl());

        item.changeColor(color.replace("\"", ""));
        itemRepository.save(item);
    }
}
