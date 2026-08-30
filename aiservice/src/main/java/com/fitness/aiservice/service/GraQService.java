package com.fitness.aiservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GraQService {

    private final WebClient webClient;

    @Value("${graq.api.key}")
    private String graQApiKey;

    @Value("${graq.api.url}")
    private String graQApiUrl;


    public String getRecommendation(String query) {
        Map<String, Object> reqBody = Map.of(
                "model", "openai/gpt-oss-20b",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", query
                        )
                }
        );

        return webClient.post()
                .uri(graQApiUrl)
                .header("Content-Type","application/json")
                .header("Authorization", "Bearer " + graQApiKey)
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
