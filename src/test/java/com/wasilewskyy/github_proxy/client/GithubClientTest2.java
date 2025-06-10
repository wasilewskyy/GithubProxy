package com.wasilewskyy.github_proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wasilewskyy.github_proxy.model.GithubRepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
@Sql(scripts = {"/init.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GithubClientTest2 {

    @Autowired
    GithubClient githubClient;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
    }

    @AfterEach
    void shutDown() {
        wireMockServer.shutdown();
    }

    @Test
    void getGithubRepositoryFromApi() throws JsonProcessingException {

        GithubRepositoryResponse githubRepositoryResponse = GithubRepositoryResponse.builder()
                .fullName("wasilewskyy/medical-clinic")
                .build();

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/wasilewskyy/medical-clinic"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(githubRepositoryResponse))));
    }
}
