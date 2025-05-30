package com.wasilewskyy.github_proxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final GithubClient githubClient;

    public RepositoryController(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @GetMapping("/{owner}/{repository}")
    public GithubRepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repository){
        return githubClient.getRepository(owner, repository);
    }
}
