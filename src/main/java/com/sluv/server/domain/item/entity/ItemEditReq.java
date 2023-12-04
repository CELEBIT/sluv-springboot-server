package com.sluv.server.domain.item.entity;

import com.sluv.server.domain.item.dto.ItemEditReqDto;
import com.sluv.server.domain.item.enums.ItemEditReqReason;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "item_edit_req")
public class ItemEditReq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_edit_req_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @NotNull
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemEditReqReason itemEditReqReason;

    @Size(max = 1002)
    private String content;

    public static ItemEditReq toEntity(User user, Item item, ItemEditReqDto dto) {
        return ItemEditReq.builder()
                .requester(user)
                .item(item)
                .itemEditReqReason(dto.getReason())
                .content(dto.getContent())
                .build();
    }
}
