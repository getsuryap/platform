package org.ospic.authentication.roles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.authentication.privileges.domains.Privilege;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.enums.RoleEnums;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
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
    private String name;


    public Role(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_privileges",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Collection<Privilege> privileges;

}