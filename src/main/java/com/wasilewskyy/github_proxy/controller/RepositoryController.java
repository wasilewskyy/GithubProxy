package com.wasilewskyy.github_proxy.controller;

import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.service.GithubRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
public class RepositoryController {

    private final GithubRepositoryService repositoryService;

    @GetMapping("/{owner}/{repository-name}")
    public GithubRepositoryDTO getRepositoryFromGithub(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        return repositoryService.getRepositoryFromGithub(owner, repositoryName);
    }

    @PostMapping("/{owner}/{repository-name}")
    @ResponseStatus(HttpStatus.CREATED)
    public GithubRepositoryDTO createRepository(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        return repositoryService.createRepository(owner, repositoryName);
    }

    @GetMapping("/local/{owner}/{repository-name}")
    public GithubRepositoryDTO getLocalRepository(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        return repositoryService.getRepository(owner, repositoryName);
    }

    @GetMapping("/local/repositories")
    public List<GithubRepositoryDTO> getAllLocalRepositories() {
        return repositoryService.getAllRepositories();
    }

    @PutMapping("/{owner}/{repository-name}")
    public GithubRepositoryDTO updateRepository(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        return repositoryService.updateRepository(owner, repositoryName);
    }

    @DeleteMapping("/{owner}/{repository-name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRepository(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        repositoryService.deleteRepository(owner, repositoryName);
    }
}