package com.sluv.server.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    @Builder
    public User(Long id) {
        this.id = id;
    }
}
