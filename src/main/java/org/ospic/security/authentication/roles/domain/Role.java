package org.ospic.security.authentication.roles.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.security.authentication.users.domain.User;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.enums.RoleEnums;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = DatabaseConstants.ROLES_TABLE)
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Table(name = DatabaseConstants.ROLES_TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = "name")})

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter(AccessLevel.PROTECTED)
    Long id;

    @Column(length = 200)
    private RoleEnums name;

    @Column(length = 200)
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(RoleEnums name, String role) {
        this.role = role;
        this.name = name;
    }

}