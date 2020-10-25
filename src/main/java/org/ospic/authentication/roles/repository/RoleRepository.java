package org.ospic.authentication.roles.repository;

import java.util.Optional;

import org.ospic.util.enums.RoleEnums;
import org.ospic.authentication.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleEnums name);

	Boolean existsByName(RoleEnums name);

}
