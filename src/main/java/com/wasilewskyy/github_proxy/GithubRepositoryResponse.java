package com.wasilewskyy.github_proxy;

import lombok.Data;

@Data
public class GithubRepositoryResponse {

    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private String createdAt;

}
