package com.sluv.domain.admin.entity;

import com.sluv.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "admin")
public class Admin extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String pwd;

    @Builder
    public Admin(Long id, String email, String name, String pwd) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }
}
