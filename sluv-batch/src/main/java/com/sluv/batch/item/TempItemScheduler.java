package com.sluv.batch.item;

import com.sluv.domain.item.service.TempItemDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Component
@Slf4j
@RequiredArgsConstructor
public class TempItemScheduler {

    private final TempItemDomainService tempItemDomainService;

    /**
     * 90일이 지난 임시 저장 아이템 제거
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void deleteExpiredTempItem() {
        log.info("Expired Temp Delete Item Time: {}", Calendar.getInstance().getTime());

        log.info("Expired Temp Delete Item");
        tempItemDomainService.deleteAllByExpiredDate(90);

    }
}
