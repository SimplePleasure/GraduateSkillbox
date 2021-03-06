package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

    @Query(value = "SELECT * FROM blog.captcha_codes WHERE secret_code = :code", nativeQuery = true)
    Optional<CaptchaCode> getCaptchaBySecretCode(@Param("code") String secretCode);
}
