package com.sluv.api.search.engine;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SearchEngine {

    List<Long> getSearchItemIds(String keyword);

    List<Long> getSearchQuestionIds(String keyword);

    List<Long> getSearchUserIds(String keyword);

}