package com.fitness.gateway.user;

import com.fitness.gateway.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient userValidationWebClient;

    public Mono<Boolean> validateUser(String userId) {
        log.info("Calling user Service : {}", userId);
        return userValidationWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {

                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(
                                new RuntimeException("User Not Found: " + userId)
                        );
                    }

                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(
                                new RuntimeException("Invalid User ID: " + userId)
                        );
                    }

                    return Mono.error(
                            new RuntimeException("Unexpected Error for user: " + userId)
                    );
                });
    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Calling user Registration: {}", registerRequest.getEmail());
        return userValidationWebClient.post()
                .uri("/api/users/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(
                                new RuntimeException("Bad Request: " + e.getMessage())
                        );
                    }

                    return Mono.error(
                            new RuntimeException("Unexpected Error: " + e.getMessage())
                    );
                });
    }
}