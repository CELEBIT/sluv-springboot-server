package com.sluv.server.domain.notice.service;

import static com.sluv.server.fixture.NoticeFixture.공지_생성;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.notice.dto.NoticeDetailResDto;
import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

//    @DisplayName("공지사항 리스트를 조회한다.")
//    @Test
//    void getAllNoticeTest() {
//        // given
//        Notice notice1 = 공지_생성("NOTICE1", "공지사항");
//        Notice notice2 = 공지_생성("NOTICE2", "공지사항");
//        noticeRepository.saveAll(List.of(notice1, notice2));
//
//        PageRequest pageable = PageRequest.of(0, 1);
//
//        // when
//        PaginationResDto<NoticeSimpleResDto> allNotice = noticeService.getAllNotice(pageable);
//
//        // then
//        assertThat(allNotice.getHasNext()).isEqualTo(true);
//        assertThat(allNotice.getContent().size()).isEqualTo(1);
//        assertThat(allNotice.getContent().get(0)).extracting("title").isEqualTo("NOTICE2");
//    }

    @DisplayName("공지사항을 상세 조회한다.")
    @Test
    void getNoticeDetail() {
        // given
        Notice notice = 공지_생성("NOTICE1", "공지사항");
        noticeRepository.save(notice);

        // when
        NoticeDetailResDto noticeDetail = noticeService.getNoticeDetail(notice.getId());

        // then
        assertThat(noticeDetail).extracting("title").isEqualTo("NOTICE1");
    }
}
