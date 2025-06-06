package com.wasilewskyy.github_proxy.service;

import com.wasilewskyy.github_proxy.client.GithubClient;
import com.wasilewskyy.github_proxy.exception.RepositoryNotFoundException;
import com.wasilewskyy.github_proxy.mapper.GithubRepositoryMapper;
import com.wasilewskyy.github_proxy.model.GithubRepository;
import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import com.wasilewskyy.github_proxy.repository.GithubRepositoryJPA;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GithubRepositoryService {

    private final GithubRepositoryJPA repositoryJPA;
    private final GithubRepositoryMapper githubRepositoryMapper;
    private final GithubClient githubClient;

    public GithubRepositoryDTO getRepositoryFromGithub(String owner, String repositoryName) {
        GithubRepositoryResponse gitHubApiResponse;
        try {
            gitHubApiResponse = githubClient.getRepository(owner, repositoryName);
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found.");
        }

        return githubRepositoryMapper.fromGithubResponse(gitHubApiResponse);
    }

    public GithubRepositoryDTO createRepository(String owner, String repositoryName) {

        if (!repositoryJPA.existsByFullName(GithubRepository.getFullName(owner, repositoryName))) {
            throw new RepositoryNotFoundException("Repository already exists: " + GithubRepository.getFullName(owner, repositoryName));
        }

        GithubRepositoryResponse gitHubApiResponse;
        try {
            gitHubApiResponse = githubClient.getRepository(owner, repositoryName);
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found.");
        }

        GithubRepository entity = githubRepositoryMapper.fromGithubResponseToEntity(gitHubApiResponse);
        GithubRepository savedEntity = repositoryJPA.save(entity);

        return githubRepositoryMapper.toDto(savedEntity);
    }

    @Transactional
    public GithubRepositoryDTO getRepository(String owner, String repositoryName) {

        GithubRepository entity = repositoryJPA.findAll().stream()
                .filter(repo -> repo.getFullName().equals(GithubRepository.getFullName(owner, repositoryName)))
                .findFirst()
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found."));

        return githubRepositoryMapper.toDto(entity);
    }

    public GithubRepositoryDTO updateRepository(String owner, String repositoryName) {

        if (!repositoryJPA.existsByFullName(GithubRepository.getFullName(owner, repositoryName))) {
            throw new RepositoryNotFoundException("Requested repository could not be found.");
        }

        GithubRepositoryResponse gitHubApiResponse;
        try {
            gitHubApiResponse = githubClient.getRepository(owner, repositoryName);
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found.");
        }

        GithubRepository existingEntity = repositoryJPA.findAll().stream()
                .filter(repo -> repo.getFullName().equals(GithubRepository.getFullName(owner, repositoryName)))
                .findFirst()
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found."));

        GithubRepositoryDTO dto = githubRepositoryMapper.fromGithubResponse(gitHubApiResponse);
        dto.setId(existingEntity.getId());

        GithubRepository updatedEntity = githubRepositoryMapper.toEntity(dto);
        GithubRepository savedEntity = repositoryJPA.save(updatedEntity);

        return githubRepositoryMapper.toDto(savedEntity);
    }

    public void deleteRepository(String owner, String repositoryName) {

        GithubRepository entity = repositoryJPA.findAll().stream()
                .filter(repo -> repo.getFullName().equals(GithubRepository.getFullName(owner, repositoryName)))
                .findFirst()
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found."));

        repositoryJPA.delete(entity);
    }
}