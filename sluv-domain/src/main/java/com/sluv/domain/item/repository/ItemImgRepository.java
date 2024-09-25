package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.impl.ItemImgRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, ItemImgRepositoryCustom {
    List<ItemImg> findAllByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);
}
