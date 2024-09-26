package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.ItemImg;

public interface ItemImgRepositoryCustom {
    ItemImg findMainImg(Long itemId);
}