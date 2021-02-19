package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM blog.users WHERE email = :email", nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM blog.users WHERE code = :code", nativeQuery = true)
    User getUserByCode(@Param("code") String hash);
}
