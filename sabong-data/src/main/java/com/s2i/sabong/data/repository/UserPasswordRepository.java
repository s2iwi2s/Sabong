package com.s2i.sabong.data.repository;

import com.s2i.sabong.data.domain.UserPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPasswordRepository extends JpaRepository<UserPasswordEntity, Long> {
}
