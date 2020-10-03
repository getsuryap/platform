package com.context.springsecurity.repository;

import java.util.Optional;

import com.context.springsecurity.util.enums.RoleEnums;
import com.context.springsecurity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleEnums name);

	Boolean existsByName(RoleEnums name);
}
