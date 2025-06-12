package com.wasilewskyy.github_proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wasilewskyy.github_proxy.model.GithubRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
public class GithubClientTest {

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
        wireMockServer.stop();
    }

    @Test
    void shouldReturnRepository() throws JsonProcessingException {
        GithubRepository githubRepository = GithubRepository.builder()
                .fullName("wasilewskyy/medical-clinic")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/wasilewskyy/medical-clinic"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(githubRepository))
                ));

        var result = githubClient.getRepository("wasilewskyy", "medical-clinic");

        assertAll(
                () -> assertEquals("wasilewskyy/medical-clinic", result.getFullName())
        );
    }
}
