package com.wasilewskyy.github_proxy.controller;

import com.wasilewskyy.github_proxy.client.GithubClient;
import com.wasilewskyy.github_proxy.model.GithubRepositoryEntity;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import com.wasilewskyy.github_proxy.service.GithubRepositoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final GithubClient githubClient;
    private final GithubRepositoryService githubService;

    public RepositoryController(GithubClient githubClient, GithubRepositoryService githubService) {
        this.githubClient = githubClient;
        this.githubService = githubService;
    }

    @GetMapping("/{owner}/{repository}")
    public GithubRepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repository){
        return githubClient.getRepository(owner, repository);
    }
}
