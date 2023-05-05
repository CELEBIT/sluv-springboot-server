package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.ItemImg;

public interface ItemImgRepositoryCustom {
    ItemImg findMainImg(Long itemId);
}
