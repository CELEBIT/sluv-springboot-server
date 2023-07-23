package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.brand.mapper.NewBrandMapper;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewBrandService {
    private final NewBrandRepository newBrandRepository;
    private final NewBrandMapper newBrandMapper;

    public NewBrandPostResDto postNewBrand(NewBrandPostReqDto dto){

        NewBrand newBrand = newBrandRepository.save(
                newBrandMapper.toEntity(dto, null)
        );

        return newBrandMapper.toNewBrandPostResDto(newBrand);
    }



}
