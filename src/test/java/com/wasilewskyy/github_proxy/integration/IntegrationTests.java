package com.wasilewskyy.github_proxy.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wasilewskyy.github_proxy.model.GithubRepository;
import com.wasilewskyy.github_proxy.model.GithubRepositoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8089)
public class IntegrationTests {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    int appPort;

    @Test
    void getDetails_shouldReturnDetails() throws JsonProcessingException {
        String owner = "wasilewskyy";
        String repository = "medical-clinic";

        GithubRepository githubRepository = GithubRepository.builder()
                .id(123L)
                .fullName("wasilewskyy/medical-clinic")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/wasilewskyy/medical-clinic"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(githubRepository))
                ));
        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repository);
        ResponseEntity<GithubRepositoryDTO> response = restTemplate.getForEntity(url, GithubRepositoryDTO.class);

        assertAll(
                () -> assertEquals(123L, response.getBody().getId()),
                () -> assertEquals("wasilewskyy/medical-clinic", response.getBody().getFullName())
        );

    }
}
