package com.wasilewskyy.github_proxy.client;

import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "GithubClient", url = "https://api.github.com")
public interface GithubClient {

    @GetMapping( "/repos/{owner}/{repository}")
    GithubRepositoryResponse getRepository(@PathVariable("owner") String owner, @PathVariable("repository") String repositoryName);

}