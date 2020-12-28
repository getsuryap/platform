package org.ospic.security.authentication.roles.repository;

import java.util.Optional;

import org.ospic.security.authentication.roles.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);

	Boolean existsByName(String name);

}
