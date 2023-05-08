package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.user.entity.User;

public interface ItemReportRepositoryCustom {
    Boolean findExistence(User user, Item target);
}
