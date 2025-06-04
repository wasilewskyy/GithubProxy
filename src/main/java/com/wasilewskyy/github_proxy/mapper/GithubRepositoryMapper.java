package com.wasilewskyy.github_proxy.mapper;

import com.wasilewskyy.github_proxy.model.GithubRepository;
import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GithubRepositoryMapper {

    GithubRepository toEntity(GithubRepositoryDTO dto);

    GithubRepositoryDTO toDto(GithubRepository entity);

    GithubRepositoryDTO fromGithubResponse(GithubRepositoryResponse githubResponse);
}