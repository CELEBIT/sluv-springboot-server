package com.sluv.domain.item.service.hashtag;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.domain.item.repository.hashtag.TempItemHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempItemHashtagDomainService {

    private final TempItemHashtagRepository tempItemHashtagRepository;

    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemHashtagRepository.deleteAllByTempItemId(tempItemId);
    }

    public void saveTempItemHashtag(TempItem saveTempItem, Hashtag tag) {
        TempItemHashtag tempItemHashtag = TempItemHashtag.toEntity(saveTempItem, tag);
        tempItemHashtagRepository.save(tempItemHashtag);
    }

    public List<TempItemHashtag> getTempItemHashtags(List<TempItem> tempItems) {
        return tempItemHashtagRepository.findAllByTempItems(tempItems);
    }
}
