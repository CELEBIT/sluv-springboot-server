package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.ItemEditReq;

public interface ItemEditReqRepositoryCustom {
    ItemEditReq findByIdWithItem(Long editReqId);
}
