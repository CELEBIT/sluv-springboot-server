package com.sluv.server.domain.question.service;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.QuestionFindPostReqDto;
import com.sluv.server.domain.question.dto.QuestionFindPostResDto;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionImg;
import com.sluv.server.domain.question.entity.QuestionItem;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.question.repository.QuestionItemRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public QuestionFindPostResDto postQuestionFind(User user, QuestionFindPostReqDto dto) {
        /**
         * 1. QuestionFind 저장
         * 2. QuestionImg 저장
         * 3. QuestionItem 저장
         */

        // 1. QuestionFind 저장
        QuestionFind questionFind = QuestionFind.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .searchNum(0L)
                .celebId(dto.getCelebId())
                .build();

        QuestionFind newQuestionFind = questionRepository.save(questionFind);

        // 2. QuestionImg 저장
        List<QuestionImg> imgList = dto.getImgList().stream().map(imgDto -> QuestionImg.builder()
                .question(questionFind)
                .imgUrl(imgDto.getImgUrl())
                .representFlag(imgDto.getRepresentFlag())
                .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                .build()
        ).toList();

        questionImgRepository.saveAll(imgList);

        // 3. QuestionItem 저장
        List<QuestionItem> itemList = dto.getItemList().stream().map(itemDto -> {
                    Item item = itemRepository.findById(itemDto.getItemId()).orElseThrow(ItemNotFoundException::new);
                    return QuestionItem.builder()
                            .question(questionFind)
                            .item(item)
                            .representFlag(itemDto.getRepresentFlag())
                            .build();
                }
        ).toList();

        questionItemRepository.saveAll(itemList);

        return QuestionFindPostResDto.builder()
                .id(newQuestionFind.getId())
                .build();

    }
}
