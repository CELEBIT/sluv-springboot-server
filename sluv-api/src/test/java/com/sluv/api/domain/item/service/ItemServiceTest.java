//package com.sluv.server.domain.item.service;
//
//import static com.sluv.server.fixture.BrandFixture.브랜드_생성;
//import static com.sluv.server.fixture.CelebFixture.셀럽_생성;
//import static com.sluv.server.fixture.CelebFixture.셀럽_카테고리_생성;
//import static com.sluv.server.fixture.ItemFixture.기본_아이템_생성;
//import static com.sluv.server.fixture.ItemFixture.아이템_카테고리_생성;
//import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.sluv.server.domain.brand.entity.Brand;
//import com.sluv.server.domain.brand.repository.BrandRepository;
//import com.sluv.server.domain.celeb.entity.Celeb;
//import com.sluv.server.domain.celeb.entity.CelebCategory;
//import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
//import com.sluv.server.domain.celeb.repository.CelebRepository;
//import com.sluv.server.domain.item.dto.ItemImgResDto;
//import com.sluv.server.domain.item.dto.ItemPostReqDto;
//import com.sluv.server.domain.item.entity.Item;
//import com.sluv.server.domain.item.entity.ItemCategory;
//import com.sluv.server.domain.item.entity.ItemImg;
//import com.sluv.server.domain.item.repository.ItemCategoryRepository;
//import com.sluv.server.domain.item.repository.ItemImgRepository;
//import com.sluv.server.domain.item.repository.ItemRepository;
//import com.sluv.server.domain.user.entity.User;
//import com.sluv.server.domain.user.repository.UserRepository;
//import com.sluv.server.global.ai.AiModelRepository;
//import com.sluv.server.global.ai.AiModelService;
//import com.sluv.server.global.cache.CacheService;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//@SpringBootTest
//public class ItemServiceTest {
//
//    @Autowired
//    private ItemService itemService;
//
//    @Mock
//    private CacheService cacheService;
//
//    @InjectMocks
//    private AiModelService aiModelService;
//
//    @MockBean
//    private AiModelRepository aiModelRepository;
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
//    private ItemImgRepository itemImgRepository;
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
//        itemImgRepository.deleteAll();
//        itemRepository.deleteAll();
//        itemCategoryRepository.deleteAll();
//        celebRepository.deleteAll();
//        celebCategoryRepository.deleteAll();
//        userRepository.deleteAll();
//        brandRepository.deleteAll();
//    }
//
//    @DisplayName("아이템을 등록한다.")
//    @Test
//    void postItemTest() {
//        //given
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
//        ItemImgResDto itemImgResDto = new ItemImgResDto("http://image", true, 0);
//
//        ItemPostReqDto itemPostReqDto = new ItemPostReqDto(null, List.of(itemImgResDto), celeb.getId(),
//                null, null, itemCategory.getId(),
//                brand.getId(), "item", 1000, null, null, null, null,
//                null, null, null);
//
//        //when
//        itemService.postItem(user, itemPostReqDto);
//
//        //then
//        assertThat(itemRepository.findAll()).hasSize(1);
//    }
//
//    @DisplayName("아이템을 수정한다.")
//    @Test
//    void fixItemTest() {
//        //given
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
//        ItemImgResDto itemImgResDto = new ItemImgResDto("http://image", true, 0);
//        ItemImg itemImg = ItemImg.toEntity(item, itemImgResDto);
//        itemImgRepository.save(itemImg);
//
//        ItemImg mainImg = itemImgRepository.findMainImg(1L);
//        System.out.println(mainImg.getItemImgUrl());
//
//        ItemPostReqDto itemPostReqDto = new ItemPostReqDto(1L, List.of(itemImgResDto), celeb.getId(),
//                null, null, itemCategory.getId(),
//                brand.getId(), "fixedItem", 1000, null, null, null, null,
//                null, null, null);
//
//        when(aiModelRepository.getItemColor(any(String.class))).thenReturn("color");
//
//        //when
//        itemService.postItem(user, itemPostReqDto);
//
//        //then
//        assertThat(itemRepository.findAll()).hasSize(1);
//        assertThat(itemRepository.findById(1L).orElse(null)).extracting("name").isEqualTo("fixedItem");
//    }
//}
