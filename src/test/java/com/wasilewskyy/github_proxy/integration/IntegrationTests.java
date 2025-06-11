package com.wasilewskyy.github_proxy.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wasilewskyy.github_proxy.model.GithubRepository;
import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import com.wasilewskyy.github_proxy.repository.GithubRepositoryJPA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8089)
@Sql(scripts = {"/init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GithubRepositoryJPA githubRepository;

    @LocalServerPort
    int appPort;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
    }

    @AfterEach
    void shutDown() {
        wireMockServer.shutdown();
    }

    @Test
    void getRepository_ShouldReturnRepositoryDetails() throws JsonProcessingException {
        String owner = "wasilewskyy";
        String repositoryName = "medical-clinic";

        GithubRepository githubRepository = GithubRepository.builder()
                .fullName("wasilewskyy/medical-clinic")
                .description("description1")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/wasilewskyy/medical-clinic"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(githubRepository))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repositoryName);
        ResponseEntity<GithubRepositoryDTO> response = restTemplate.getForEntity(url, GithubRepositoryDTO.class);

        assertAll(
                () -> assertEquals("wasilewskyy/medical-clinic", response.getBody().getFullName()),
                () -> assertEquals("description1", response.getBody().getDescription())
        );
    }

    @Test
    void getLocalRepository_ShouldReturnTest1FromDatabase() {
        String owner = "wasilewskyy";
        String repositoryName = "test1";

        String url = String.format("http://localhost:%s/localrepositories/%s/%s", appPort, owner, repositoryName);
        ResponseEntity<GithubRepositoryDTO> response = restTemplate.getForEntity(url, GithubRepositoryDTO.class);

        assertAll(
                () -> assertEquals("wasilewskyy/test1", response.getBody().getFullName()),
                () -> assertEquals("description1", response.getBody().getDescription()),
                () -> assertEquals("url1", response.getBody().getCloneUrl()),
                () -> assertEquals(1, response.getBody().getStars())
        );
    }

    @Test
    void createRepository_ShouldCreateTest2Repository() throws JsonProcessingException {
        String owner = "wasilewskyy";
        String repositoryName = "test2";

        GithubRepository createdRepo = GithubRepository.builder()
                .id(2L)
                .fullName("wasilewskyy/test2")
                .description("description2")
                .cloneUrl("url2")
                .stars(2)
                .createdAt("2025-06-02")
                .build();

        wireMockServer.stubFor(WireMock.post(WireMock.urlPathEqualTo("/repos/wasilewskyy/test2"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(createdRepo))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repositoryName);
        ResponseEntity<GithubRepositoryDTO> response = restTemplate.postForEntity(url, null, GithubRepositoryDTO.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(2L, response.getBody().getId()),
                () -> assertEquals("wasilewskyy/test2", response.getBody().getFullName()),
                () -> assertEquals("description2", response.getBody().getDescription()),
                () -> assertEquals("url2", response.getBody().getCloneUrl()),
                () -> assertEquals(2, response.getBody().getStars())
        );
    }

    @Test
    void updateRepository_ShouldUpdateTest3Repository() throws JsonProcessingException {
        String owner = "wasilewskyy";
        String repositoryName = "test3";

        GithubRepository updatedRepo = GithubRepository.builder()
                .id(3L)
                .fullName("wasilewskyy/test3")
                .description("updated description3")
                .cloneUrl("updated_url3")
                .stars(50)
                .createdAt("2025-06-03")
                .build();

        wireMockServer.stubFor(WireMock.put(WireMock.urlPathEqualTo("/repos/wasilewskyy/test5"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(updatedRepo))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repositoryName);
        HttpEntity<String> requestEntity = new HttpEntity<>("");
        ResponseEntity<GithubRepositoryDTO> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, GithubRepositoryDTO.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(3L, response.getBody().getId()),
                () -> assertEquals("wasilewskyy/test3", response.getBody().getFullName()),
                () -> assertEquals("updated description3", response.getBody().getDescription()),
                () -> assertEquals("updated_url3", response.getBody().getCloneUrl()),
                () -> assertEquals(50, response.getBody().getStars())
        );
    }

    @Test
    void deleteRepository_ShouldDeleteTest1Repository() {
        String owner = "wasilewskyy";
        String repositoryName = "test1";

        wireMockServer.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/repos/wasilewskyy/test1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repositoryName);
        restTemplate.delete(url);
    }

    @Test
    void getRepository_ShouldReturn404_WhenRepositoryNotFound() {
        String owner = "wasilewskyy";
        String repositoryName = "nonexistent";

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/wasilewskyy/nonexistent"))
                .willReturn(WireMock.aResponse()
                        .withStatus(404)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"message\":\"Not Found\"}")
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repositoryName);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}