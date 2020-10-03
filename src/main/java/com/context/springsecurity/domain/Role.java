package com.context.springsecurity.domain;

import com.context.springsecurity.util.constants.DatabaseConstants;
import com.context.springsecurity.util.enums.RoleEnums;

import javax.persistence.*;

@Entity
@Table(name = DatabaseConstants.ROLES_TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = "name")})

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 200)
    private RoleEnums name;

    public Role() { }

    public Role(RoleEnums name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnums getName() {
        return name;
    }

    public void setName(RoleEnums name) {
        this.name = name;
    }
}