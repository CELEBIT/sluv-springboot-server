//package com.sluv.server.domain.item.service;
//
//import static com.sluv.server.fixture.BrandFixture.브랜드_생성;
//import static com.sluv.server.fixture.CelebFixture.셀럽_생성;
//import static com.sluv.server.fixture.CelebFixture.셀럽_카테고리_생성;
//import static com.sluv.server.fixture.ItemFixture.기본_아이템_생성;
//import static com.sluv.server.fixture.ItemFixture.아이템_카테고리_생성;
//import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.sluv.server.domain.brand.entity.Brand;
//import com.sluv.server.domain.brand.repository.BrandRepository;
//import com.sluv.server.domain.celeb.entity.Celeb;
//import com.sluv.server.domain.celeb.entity.CelebCategory;
//import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
//import com.sluv.server.domain.celeb.repository.CelebRepository;
//import com.sluv.server.domain.item.dto.HashtagRequestDto;
//import com.sluv.server.domain.item.dto.HashtagResponseDto;
//import com.sluv.server.domain.item.entity.Item;
//import com.sluv.server.domain.item.entity.ItemCategory;
//import com.sluv.server.domain.item.entity.hashtag.Hashtag;
//import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
//import com.sluv.server.domain.item.repository.ItemCategoryRepository;
//import com.sluv.server.domain.item.repository.ItemRepository;
//import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
//import com.sluv.server.domain.item.repository.hashtag.ItemHashtagRepository;
//import com.sluv.server.domain.user.entity.User;
//import com.sluv.server.domain.user.repository.UserRepository;
//import com.sluv.server.global.common.response.PaginationResDto;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//
//@SpringBootTest
//public class HashtagServiceTest {
//
//    @Autowired
//    private HashtagService hashtagService;
//
//    @Autowired
//    private HashtagRepository hashtagRepository;
//
//    @Autowired
//    private ItemHashtagRepository itemHashtagRepository;
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ItemCategoryRepository itemCategoryRepository;
//
//    @Autowired
//    private CelebCategoryRepository celebCategoryRepository;
//
//    @Autowired
//    private CelebRepository celebRepository;
//
//    @Autowired
//    private BrandRepository brandRepository;
//
//    @AfterEach
//    void clear() {
//
//        itemHashtagRepository.deleteAll();
//        hashtagRepository.deleteAll();
//        itemRepository.deleteAll();
//        itemCategoryRepository.deleteAll();
//        celebRepository.deleteAll();
//        celebCategoryRepository.deleteAll();
//        userRepository.deleteAll();
//        brandRepository.deleteAll();
//    }
//
//    @DisplayName("이름으로 해시태그-개수를 조회한다.")
//    @Test
//    void getHashtagTest() {
//        //given
//        Hashtag hashtag1 = Hashtag.toEntity("해시태그1");
//        Hashtag hashtag2 = Hashtag.toEntity("해시태그2");
//        Hashtag hashtag3 = Hashtag.toEntity("아닌태그");
//        hashtagRepository.saveAll(List.of(hashtag1, hashtag2, hashtag3));
//
//        User user = 카카오_유저_생성();
//        CelebCategory celebCategory = 셀럽_카테고리_생성("배우", null);
//        Celeb celeb = 셀럽_생성(celebCategory, "배우1", "Celeb1", null);
//        ItemCategory itemCategory = 아이템_카테고리_생성("카테고리", null);
//        Brand brand = 브랜드_생성("브랜드", "Brand");
//        userRepository.save(user);
//        celebCategoryRepository.save(celebCategory);
//        celebRepository.save(celeb);
//        itemCategoryRepository.save(itemCategory);
//        brandRepository.save(brand);
//
//        Item item = 기본_아이템_생성(user, celeb, null, itemCategory,
//                brand, null, "item", null, null, null, null);
//        itemRepository.save(item);
//
//        ItemHashtag itemHashtag1 = ItemHashtag.toEntity(item, hashtag1);
//        ItemHashtag itemHashtag2 = ItemHashtag.toEntity(item, hashtag2);
//        ItemHashtag itemHashtag3 = ItemHashtag.toEntity(item, hashtag3);
//
//        itemHashtagRepository.saveAll(List.of(itemHashtag1, itemHashtag2, itemHashtag3));
//
//        PageRequest pageable = PageRequest.of(0, 1);
//        //when
//        PaginationResDto<HashtagResponseDto> hashtagResponseDtos = hashtagService.getHashtag("해시", pageable);
//
//        //then
//        assertThat(hashtagResponseDtos.getContent()).hasSize(1);
//        assertThat(hashtagResponseDtos.getHasNext()).isEqualTo(true);
//        assertThat(hashtagResponseDtos.getContent().get(0)).extracting("hashtagContent").isEqualTo("해시태그1");
//        assertThat(hashtagResponseDtos.getContent().get(0)).extracting("count").isEqualTo(1L);
//
//    }
//
//    @DisplayName("해시태그를 등록한다.")
//    @Test
//    void postHashtagTest() {
//        //given
//        HashtagRequestDto hashtagDto = new HashtagRequestDto("해시태그");
//
//        //when
//        hashtagService.postHashtag(hashtagDto);
//
//        //then
//        assertThat(hashtagRepository.findAll()).hasSize(1);
//        assertThat(hashtagRepository.findAll().get(0)).extracting("content").isEqualTo("해시태그");
//    }
//}
