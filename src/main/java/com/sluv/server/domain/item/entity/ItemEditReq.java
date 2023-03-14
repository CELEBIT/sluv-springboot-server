package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemEditReqReason;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "item_edit_req")
public class ItemEditReq extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_edit_req_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @NotNull
    private User requester;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private ItemEditReqReason itemEditReqReason;

    @Size(max = 1002)
    private String content;

    @Builder
    public ItemEditReq(Long id, User requester, Item item, ItemEditReqReason itemEditReqReason, String content) {
        this.id = id;
        this.requester = requester;
        this.item = item;
        this.itemEditReqReason = itemEditReqReason;
        this.content = content;
    }
}
