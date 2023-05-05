package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.impl.ItemImgRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, ItemImgRepositoryCustom {
    List<ItemImg> findAllByItemId(Long itemId);
}
