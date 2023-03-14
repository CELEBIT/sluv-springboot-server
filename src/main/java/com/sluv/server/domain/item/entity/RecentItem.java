package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recent_item")
public class RecentItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;


    @Builder
    public RecentItem(Long id, Item item, User user) {
        this.id = id;
        this.item = item;
        this.user = user;
    }
}
