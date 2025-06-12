package com.wasilewskyy.github_proxy.client;

import com.wasilewskyy.github_proxy.config.FeignConfig;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GithubClient", url = "${spring.cloud.openfeign.client.config.githubClient.url}", configuration = FeignConfig.class)
public interface GithubClient {

    @GetMapping( "/repos/{owner}/{repository}")
    GithubRepositoryResponse getRepository(@PathVariable("owner") String owner, @PathVariable("repository") String repositoryName);

}