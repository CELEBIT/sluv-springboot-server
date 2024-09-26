package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.CelebActivity;
import com.sluv.domain.celeb.repository.CelebActivityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CelebActivityDomainService {

    private final CelebActivityRepository celebActivityRepository;

    @Transactional(readOnly = true)
    public List<CelebActivity> findAllByCelebId(Long celebId) {
        return celebActivityRepository.findAllByCelebId(celebId);
    }

}
