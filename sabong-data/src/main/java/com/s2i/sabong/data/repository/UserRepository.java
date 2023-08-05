package com.s2i.sabong.data.repository;

import com.s2i.sabong.data.domain.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";
    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<UserEntity> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<UserEntity> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<UserEntity> findOneWithAuthoritiesByEmailIgnoreCase(String email);
}
