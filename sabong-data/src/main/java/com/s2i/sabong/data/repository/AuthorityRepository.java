package com.s2i.sabong.data.repository;

import com.s2i.sabong.data.domain.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity,String> {
}
