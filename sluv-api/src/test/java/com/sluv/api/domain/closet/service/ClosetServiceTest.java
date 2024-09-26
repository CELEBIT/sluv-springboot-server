//package com.sluv.server.domain.closet.service;
//
//import static com.sluv.server.fixture.ClosetFixture.공개_옷장_생성;
//import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
//
//import com.sluv.server.domain.closet.entity.Closet;
//import com.sluv.server.domain.closet.repository.ClosetRepository;
//import com.sluv.server.domain.user.entity.User;
//import com.sluv.server.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class ClosetServiceTest {
//
//    @Autowired
//    private ClosetService closetService;
//
//    @Autowired
//    private ClosetRepository closetRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @AfterEach
//    void clear() {
//        closetRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @DisplayName("옷장 리스트를 조회한다.")
//    @Test
//    void getClosetListTest() {
//        //given
//        User user = 카카오_유저_생성();
//        Closet closet1 = 공개_옷장_생성(user, "옷장1");
//        //when
//
//        //then
//    }
//}
