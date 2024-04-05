package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemEditReq;
import com.sluv.server.domain.item.repository.impl.ItemEditReqRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEditReqRepository extends JpaRepository<ItemEditReq, Long>, ItemEditReqRepositoryCustom {
}
