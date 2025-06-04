package com.wasilewskyy.github_proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepositoryDTO {

    private Long id;
    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private String createdAt;
}
