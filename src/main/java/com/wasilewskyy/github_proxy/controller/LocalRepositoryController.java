package com.wasilewskyy.github_proxy.controller;

import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.service.GithubRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localrepositories")
@RequiredArgsConstructor
public class LocalRepositoryController {

    private final GithubRepositoryService repositoryService;

    @GetMapping("/{owner}/{repository-name}")
    public GithubRepositoryDTO getLocalRepository(@PathVariable String owner, @PathVariable("repository-name") String repositoryName) {
        return repositoryService.getRepository(owner, repositoryName);
    }
}
