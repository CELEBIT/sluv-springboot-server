package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewBrandService {
    private final NewBrandRepository newBrandRepository;

    public NewBrandPostResDto postNewBrand(NewBrandPostReqDto dto) {

        NewBrand newBrand = newBrandRepository.save(NewBrand.toEntity(dto));

        return NewBrandPostResDto.of(newBrand);
    }


}
