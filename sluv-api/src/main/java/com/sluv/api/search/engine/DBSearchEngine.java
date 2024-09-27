package com.sluv.api.search.engine;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.repository.ItemRepository;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.repository.QuestionRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DBSearchEngine implements SearchEngine {

    private final ItemRepository itemRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;


    @Override
    public List<Long> getSearchItemIds(String keyword) {
        List<String> keywords = Arrays.stream(keyword.split(" ")).collect(Collectors.toList());
        List<Long> result = new ArrayList<>();

        if (keywords.contains("청바지")) {
            keywords.add("데님바지");
        }

        for (String word : keywords) {
            List<Long> itemIds = itemRepository.getSearchItemIds(word)
                    .stream()
                    .map(Item::getId)
                    .toList();

            result.addAll(itemIds);
        }
        return result;
    }

    @Override
    public List<Long> getSearchQuestionIds(String keyword) {
        List<String> keywords = Arrays.stream(keyword.split(" ")).toList();
        List<Long> result = new ArrayList<>();

        for (String word : keywords) {
            List<Question> searchQuestions = questionRepository.getSearchQuestionIds(word);
            List<Long> searchQuestionIds = searchQuestions.stream().map(Question::getId).distinct().toList();
            searchQuestions.sort(Comparator.comparing(Question::getCreatedAt).reversed());
            result.addAll(searchQuestionIds);
        }

        return result;
    }

    @Override
    public List<Long> getSearchUserIds(String keyword) {
        List<String> keywords = Arrays.stream(keyword.split(" ")).toList();
        List<Long> result = new ArrayList<>();

        for (String word : keywords) {
            List<Long> searchUserIds = userRepository.getSearchUserIds(word).stream().map(User::getId).toList();
            result.addAll(searchUserIds);
        }

        return result;
    }
}