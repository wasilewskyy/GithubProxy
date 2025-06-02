package com.wasilewskyy.github_proxy.mapper;

import com.wasilewskyy.github_proxy.model.GithubRepositoryEntity;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;

@Mapper(componentModel = "spring")
public interface GithubRepositoryMapper {

    GithubRepositoryEntity toEntity(GithubRepositoryResponse dto);

    GithubRepositoryResponse toDto(GithubRepositoryEntity entity);
}