package com.wasilewskyy.github_proxy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "repositories")
public class GithubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private String createdAt;

    public static String getFullName(String owner, String repositoryName) {
        return owner + "/" + repositoryName;
    }
}
