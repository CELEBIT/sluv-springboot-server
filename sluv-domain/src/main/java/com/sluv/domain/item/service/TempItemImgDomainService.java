package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import com.sluv.domain.item.repository.TempItemImgRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempItemImgDomainService {

    private final TempItemImgRepository tempItemImgRepository;

    @Transactional
    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemImgRepository.deleteAllByTempItemId(tempItemId);
    }

    @Transactional
    public void saveTempItemImg(TempItem saveTempItem, ItemImgDto itemImgDto) {
        TempItemImg tempItemImg = TempItemImg.toEntity(saveTempItem, itemImgDto);
        tempItemImgRepository.save(tempItemImg);
    }

    @Transactional
    public List<TempItemImg> getTempItemImages(List<TempItem> tempItems) {
        return tempItemImgRepository.findAllByTempItems(tempItems);
    }

}
