package com.wasilewskyy.github_proxy.service;

import com.wasilewskyy.github_proxy.mapper.GithubRepositoryMapper;
import com.wasilewskyy.github_proxy.model.GithubRepositoryEntity;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import com.wasilewskyy.github_proxy.repository.GithubRepository;
import org.springframework.stereotype.Service;

@Service
public class GithubRepositoryService {

    private final GithubRepository githubRepository;
    private final GithubRepositoryMapper mapper;

    public GithubRepositoryService(GithubRepository githubRepository, GithubRepositoryMapper mapper) {
        this.githubRepository = githubRepository;
        this.mapper = mapper;
    }

    public GithubRepositoryEntity save(GithubRepositoryResponse dto) {
        GithubRepositoryEntity entity = mapper.toEntity(dto);
        return githubRepository.save(entity);
    }

    public GithubRepositoryEntity find(Long id) {
        return githubRepository.findById(id)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository with id " + id + " not found"));
    }

    public void delete(Long id) {
        GithubRepositoryEntity entity = find(id);
        githubRepository.delete(entity);
    }
}
