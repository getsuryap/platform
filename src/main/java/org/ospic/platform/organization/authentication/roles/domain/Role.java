package org.ospic.platform.organization.authentication.roles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.ospic.platform.organization.authentication.roles.privileges.domains.Privilege;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity(name = DatabaseConstants.ROLES_TABLE)
@Data
@NoArgsConstructor
@Table(name = DatabaseConstants.ROLES_TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Role  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter(AccessLevel.PROTECTED)
    Long id;

    @Column(length = 200)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;


    public Role(String name) {
        this.name = name;
    }

}