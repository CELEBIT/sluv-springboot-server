package com.sluv.domain.item.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSaveDto {

    private LocalDateTime whenDiscovery;
    private String whereDiscovery;
    private String itemName;
    private Integer price;
    private String additionalInfo;
    private String infoSource;

}
