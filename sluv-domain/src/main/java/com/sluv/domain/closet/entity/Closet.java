package com.sluv.domain.closet.entity;

import com.sluv.domain.closet.dto.ClosetDomainDto;
import com.sluv.domain.closet.enums.ClosetColor;
import com.sluv.domain.closet.enums.ClosetStatus;
import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "closet")
public class Closet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    @Size(max = 45)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String coverImgUrl;

    @Enumerated(EnumType.STRING)
    private ClosetColor color;

    @NotNull
    @ColumnDefault("0")
    private Boolean basicFlag;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'PUBLIC'")
    private ClosetStatus closetStatus;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ItemScrap> itemScraps;

    public void changeCloset(ClosetDomainDto dto) {
        this.name = dto.getName();
        this.coverImgUrl = dto.getCoverImgUrl();
        this.color = dto.getColorScheme();
        this.closetStatus = dto.getClosetStatus();
    }

    public static Closet toEntity(User user, ClosetDomainDto dto) {
        return Closet.builder()
                .user(user)
                .name(dto.getName())
                .coverImgUrl(dto.getCoverImgUrl())
                .color(dto.getColorScheme())
                .basicFlag(false)
                .closetStatus(dto.getClosetStatus())
                .build();
    }

    public static Closet createBasic(User user) {
        return Closet.builder()
                .user(user)
                .name("기본 옷장")
                .coverImgUrl(null)
                .basicFlag(true)
                .color(ClosetColor.PURPLE)
                .closetStatus(ClosetStatus.PRIVATE)
                .build();
    }

}
