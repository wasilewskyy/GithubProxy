package com.wasilewskyy.github_proxy.repository;

import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GithubRepositoryJPA extends JpaRepository<com.wasilewskyy.github_proxy.model.GithubRepository, Long> {
    Optional<GithubRepositoryResponse> findByFullName(String fullName);
    boolean existsByFullName(String fullName);
}
