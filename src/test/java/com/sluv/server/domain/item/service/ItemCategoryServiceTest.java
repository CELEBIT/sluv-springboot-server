//package com.sluv.server.domain.item.service;
//
//import static com.sluv.server.fixture.ItemFixture.아이템_카테고리_생성;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.sluv.server.domain.item.dto.ItemCategoryParentResponseDto;
//import com.sluv.server.domain.item.entity.ItemCategory;
//import com.sluv.server.domain.item.repository.ItemCategoryRepository;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class ItemCategoryServiceTest {
//
//    @Autowired
//    private ItemCategoryService itemCategoryService;
//
//    @Autowired
//    private ItemCategoryRepository itemCategoryRepository;
//
//    @AfterEach
//    void clear() {
//        itemCategoryRepository.deleteAll();
//    }
//
//    @DisplayName("아이템 카테고리를 조회한다.")
//    @Test
//    void getItemCategoryTest() {
//        //given
//        ItemCategory itemCategory1 = 아이템_카테고리_생성("상의", null);
//        ItemCategory itemCategory2 = 아이템_카테고리_생성("반팔티", itemCategory1);
//        ItemCategory itemCategory3 = 아이템_카테고리_생성("맨투맨", itemCategory1);
//        itemCategoryRepository.saveAll(List.of(itemCategory1, itemCategory2, itemCategory3));
//
//        //when
//        List<ItemCategoryParentResponseDto> itemCategory = itemCategoryService.getItemCategory();
//
//        //then
//        assertThat(itemCategory.get(0)).extracting("name").isEqualTo("상의");
//        assertThat(itemCategory.get(0).getSubCategoryList()).hasSize(2);
//    }
//
//
//}
