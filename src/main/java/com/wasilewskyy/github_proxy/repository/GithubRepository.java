package com.wasilewskyy.github_proxy.repository;

import com.wasilewskyy.github_proxy.model.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubRepository extends JpaRepository<GithubRepositoryEntity, Long> {
}
