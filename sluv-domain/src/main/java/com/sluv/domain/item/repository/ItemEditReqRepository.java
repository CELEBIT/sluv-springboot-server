package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.repository.impl.ItemEditReqRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEditReqRepository extends JpaRepository<ItemEditReq, Long>, ItemEditReqRepositoryCustom {
}
