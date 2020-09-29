package com.context.springsecurity.repository;

import com.context.springsecurity.domain.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByName(String name);

    Boolean existsByName(String name);

    Boolean existsByMssIdn(String mssIdn);

}
