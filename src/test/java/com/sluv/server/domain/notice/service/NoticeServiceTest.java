package com.sluv.server.domain.notice.service;

import static com.sluv.server.domain.notice.enums.NoticeStatus.ACTIVE;
import static com.sluv.server.domain.notice.enums.NoticeType.ETC;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.notice.dto.NoticeDetailResDto;
import com.sluv.server.domain.notice.dto.NoticeSimpleResDto;
import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.repository.NoticeRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @AfterEach
    void clear() {
        noticeRepository.deleteAll();
    }

    @DisplayName("공지사항 리스트를 조회한다.")
    @Test
    void getAllNoticeTest() {
        // given
        Notice notice1 = Notice.builder()
                .title("NOTICE1")
                .content("공지사항")
                .noticeType(ETC)
                .status(ACTIVE)
                .build();

        Notice notice2 = Notice.builder()
                .title("NOTICE2")
                .content("공지사항")
                .noticeType(ETC)
                .status(ACTIVE)
                .build();
        noticeRepository.saveAll(List.of(notice1, notice2));

        PageRequest pageable = PageRequest.of(0, 1);

        // when
        PaginationResDto<NoticeSimpleResDto> allNotice = noticeService.getAllNotice(pageable);

        // then
        assertThat(allNotice.getHasNext()).isEqualTo(true);
        assertThat(allNotice.getContent().size()).isEqualTo(1);
        assertThat(allNotice.getContent().get(0)).extracting("title").isEqualTo("NOTICE2");
    }

    @DisplayName("공지사항을 상세 조회한다.")
    @Test
    void getNoticeDetail() {
        // given
        Notice notice1 = Notice.builder()
                .title("NOTICE1")
                .content("공지사항")
                .noticeType(ETC)
                .status(ACTIVE)
                .build();
        noticeRepository.save(notice1);

        // when
        NoticeDetailResDto notice = noticeService.getNoticeDetail(notice1.getId());

        // then
        assertThat(notice).extracting("title").isEqualTo("NOTICE1");
    }
}
