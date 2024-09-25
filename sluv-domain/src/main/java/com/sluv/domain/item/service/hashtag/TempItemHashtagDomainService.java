package com.sluv.domain.item.service.hashtag;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.domain.item.repository.hashtag.TempItemHashtagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempItemHashtagDomainService {

    private final TempItemHashtagRepository tempItemHashtagRepository;

    @Transactional
    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemHashtagRepository.deleteAllByTempItemId(tempItemId);
    }

    @Transactional
    public void saveTempItemHashtag(TempItem saveTempItem, Hashtag tag) {
        TempItemHashtag tempItemHashtag = TempItemHashtag.toEntity(saveTempItem, tag);
        tempItemHashtagRepository.save(tempItemHashtag);
    }

    @Transactional(readOnly = true)
    public List<TempItemHashtag> getTempItemHashtags(List<TempItem> tempItems) {
        return tempItemHashtagRepository.findAllByTempItems(tempItems);
    }
}
