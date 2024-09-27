package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import com.sluv.domain.item.repository.TempItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempItemImgDomainService {

    private final TempItemImgRepository tempItemImgRepository;

    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemImgRepository.deleteAllByTempItemId(tempItemId);
    }

    public void saveTempItemImg(TempItem saveTempItem, ItemImgDto itemImgDto) {
        TempItemImg tempItemImg = TempItemImg.toEntity(saveTempItem, itemImgDto);
        tempItemImgRepository.save(tempItemImg);
    }

    public List<TempItemImg> getTempItemImages(List<TempItem> tempItems) {
        return tempItemImgRepository.findAllByTempItems(tempItems);
    }

}
