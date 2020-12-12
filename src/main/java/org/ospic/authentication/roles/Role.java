package org.ospic.authentication.roles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.authentication.privileges.domains.Privilege;
import org.ospic.authentication.users.User;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.enums.RoleEnums;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    private String role_id;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;


    public Role(RoleEnums name, String role_id) {
        this.role_id = role_id;
        this.name = name;
    }

}