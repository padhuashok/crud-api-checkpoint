package com.galvanize.crudapicheckpoint.repository;

import com.galvanize.crudapicheckpoint.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String email);
}
