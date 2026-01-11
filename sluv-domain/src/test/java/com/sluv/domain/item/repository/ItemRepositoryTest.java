package com.sluv.domain.item.repository;

import com.sluv.domain.closet.repository.ClosetRepository;
import com.sluv.domain.config.TestConfig;
import com.sluv.domain.item.dto.ItemCountDto;
import com.sluv.domain.item.dto.ItemStatusDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.entity.ItemLike;
import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private ItemLikeRepository itemLikeRepository;

    @Autowired
    private ClosetRepository closetRepository;

    @Autowired
    private ItemScrapRepository itemScrapRepository;

    @AfterEach
    void clean() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemCategoryRepository.deleteAll();
        itemLikeRepository.deleteAll();
        closetRepository.deleteAll();
        itemScrapRepository.deleteAll();
    }
    
    @Test
    @DisplayName("아이템 Count 데이터 조회")
    void getCountDataByItemId() {
        // given
        User user1 = User.builder()
                .email("user1@example.com")
                .snsType(SnsType.ETC)
                .nickname("u1")
                .userStatus(UserStatus.PENDING_PROFILE)
                .ageRange(UserAge.TWENTIES)
                .gender(UserGender.FEMALE)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("user2@example.com")
                .snsType(SnsType.ETC)
                .nickname("u2")
                .userStatus(UserStatus.PENDING_PROFILE)
                .ageRange(UserAge.TWENTIES)
                .gender(UserGender.MALE)
                .build();
        userRepository.save(user2);

        ItemCategory category = ItemCategory.builder()
                .name("cat")
                .build();
        itemCategoryRepository.save(category);

        Item item = Item.builder()
                .user(user1)
                .category(category)
                .name("test item")
                .price(1000)
                .build();
        itemRepository.save(item);

        ItemLike like1 = ItemLike.toEntity(item, user1);
        ItemLike like2 = ItemLike.toEntity(item, user2);
        itemLikeRepository.save(like1);
        itemLikeRepository.save(like2);

        Closet closet = Closet.createBasic(user1);
        closetRepository.save(closet);

        ItemScrap scrap = ItemScrap.toEntity(item, closet);
        itemScrapRepository.save(scrap);

        // when
        ItemCountDto countDto = itemRepository.getCountDataByItemId(item.getId());

        // then
        assertThat(countDto).isNotNull();
        assertThat(countDto.likeCount()).isEqualTo(2);
        assertThat(countDto.scrapCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("아이템 상태 데이터 조회")
    void getStatusDataByItemId() {
        // given
        User user1 = User.builder()
                .email("user1@example.com")
                .snsType(SnsType.ETC)
                .nickname("u1")
                .userStatus(UserStatus.PENDING_PROFILE)
                .ageRange(UserAge.TWENTIES)
                .gender(UserGender.FEMALE)
                .build();
        userRepository.save(user1);

        ItemCategory category = ItemCategory.builder()
                .name("cat")
                .build();
        itemCategoryRepository.save(category);

        Item item = Item.builder()
                .user(user1)
                .category(category)
                .name("test item")
                .price(1000)
                .build();
        itemRepository.save(item);

        ItemLike like1 = ItemLike.toEntity(item, user1);
        itemLikeRepository.save(like1);

        Closet closet = Closet.createBasic(user1);
        closetRepository.save(closet);

        ItemScrap scrap = ItemScrap.toEntity(item, closet);
        itemScrapRepository.save(scrap);

        // when
        ItemStatusDto statusDto = itemRepository.getStatusDataByItemId(item.getId(), user1, List.of(closet.getId()));

        // then
        assertThat(statusDto).isNotNull();
        assertThat(statusDto.likeStatus()).isTrue();
        assertThat(statusDto.scrapStatus()).isTrue();
    }

}
