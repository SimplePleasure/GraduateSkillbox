package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM blog.users WHERE email = :email", nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM blog.users WHERE code = :code", nativeQuery = true)
    Optional<User> getUserByCode(@Param("code") String hash);

    @Query(value = "SELECT id FROM blog.users WHERE email = :mail", nativeQuery = true)
    int getUserId(@Param("mail") String mail);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * FROM blog.users u where u.email = :email) THEN 'TRUE' ELSE 'FALSE' END", nativeQuery = true)
    boolean isEmailAlreadyExists(@Param("email") String email);
}
