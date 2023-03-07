package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.enums.ItemEditReqReason;
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

    @NotNull
    private Long requesterId;

    @NotNull
    private Long itemId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private ItemEditReqReason itemEditReqReason;

    @Size(max = 1002)
    private String content;

    @Builder
    public ItemEditReq(Long id, Long requesterId, Long itemId, ItemEditReqReason itemEditReqReason, String content) {
        this.id = id;
        this.requesterId = requesterId;
        this.itemId = itemId;
        this.itemEditReqReason = itemEditReqReason;
        this.content = content;
    }
}
