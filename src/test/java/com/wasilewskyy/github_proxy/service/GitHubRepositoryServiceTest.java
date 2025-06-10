package com.wasilewskyy.github_proxy.service;


import com.wasilewskyy.github_proxy.client.GithubClient;
import com.wasilewskyy.github_proxy.exception.RepositoryNotFoundException;
import com.wasilewskyy.github_proxy.mapper.GithubRepositoryMapper;
import com.wasilewskyy.github_proxy.model.GithubRepository;
import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import com.wasilewskyy.github_proxy.repository.GithubRepositoryJPA;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitHubRepositoryServiceTest {

    GithubRepositoryJPA repositoryJPA;

    GithubRepositoryMapper mapper;

    GithubClient githubClient;

    GithubRepositoryService service;

    @BeforeEach
    void setup() {
        this.repositoryJPA = Mockito.mock(GithubRepositoryJPA.class);
        this.mapper = Mockito.mock(GithubRepositoryMapper.class);
        this.githubClient = Mockito.mock(GithubClient.class);
        this.service = new GithubRepositoryService(repositoryJPA, mapper, githubClient);
    }

    @Test
    void createRepository_Success() {
        // Given
        GithubRepositoryResponse response = new GithubRepositoryResponse();
        GithubRepositoryDTO dto = new GithubRepositoryDTO();
        GithubRepository entity = new GithubRepository();

        when(repositoryJPA.existsByFullName("owner/repo")).thenReturn(false);
        when(githubClient.getRepository("owner", "repo")).thenReturn(response);
        when(mapper.fromGithubResponse(response)).thenReturn(dto);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repositoryJPA.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        // When
        GithubRepositoryDTO result = service.createRepository("owner", "repo");

        // Then
        assertNotNull(result);
        verify(githubClient).getRepository("owner", "repo");
        verify(repositoryJPA).save(entity);
    }

    @Test
    void createRepository_AlreadyExists_ThrowsException() {
        // Given
        when(repositoryJPA.existsByFullName("owner/repo")).thenReturn(true);

        // When & Then
        assertThrows(RepositoryNotFoundException.class, () -> service.createRepository("owner", "repo"));
    }

    @Test
    void createRepository_GitHubNotFound_ThrowsException() {
        // Given
        when(repositoryJPA.existsByFullName("owner/repo")).thenReturn(false);
        when(githubClient.getRepository("owner", "repo"))
                .thenThrow(mock(FeignException.NotFound.class));

        // When & Then
        assertThrows(RepositoryNotFoundException.class, () -> service.createRepository("owner", "repo"));
    }

    @Test
    void getRepositoryFromGithub_Success() {
        // Given
        GithubRepositoryResponse response = new GithubRepositoryResponse();
        GithubRepositoryDTO dto = new GithubRepositoryDTO();
        when(githubClient.getRepository("owner", "repo")).thenReturn(response);
        when(mapper.fromGithubResponse(response)).thenReturn(dto);

        // When
        GithubRepositoryDTO result = service.getRepositoryFromGithub("owner", "repo");

        // Then
        assertNotNull(result);
        verify(githubClient).getRepository("owner", "repo");
    }

    @Test
    void deleteRepository_Success() {
        // Given
        GithubRepository entity = new GithubRepository();
        entity.setFullName("owner/repo");
        when(repositoryJPA.findAll()).thenReturn(Arrays.asList(entity));

        // When
        service.deleteRepository("owner", "repo");

        // Then
        verify(repositoryJPA).delete(entity);
    }

    @Test
    void deleteRepository_NotFound_ThrowsException() {
        // Given
        when(repositoryJPA.findAll()).thenReturn(Arrays.asList());

        // When & Then
        assertThrows(RepositoryNotFoundException.class, () -> service.deleteRepository("owner", "repo"));
    }
}