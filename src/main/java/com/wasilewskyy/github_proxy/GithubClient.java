package com.wasilewskyy.github_proxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "", url = "")
public interface GithubClient {

    @GetMapping("/repositories/{owner}/{repository-name}")
    GithubRepositoryResponse getRepository(@PathVariable("owner") String owner, @PathVariable("repository") String repositoryName);

}
