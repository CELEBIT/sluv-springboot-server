package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.user.entity.User;

public interface ItemReportRepositoryCustom {
    Boolean findExistence(User user, Item target);
}
