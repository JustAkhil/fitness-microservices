package com.fitness.gateway;

import com.fitness.gateway.user.UserService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {

        String userId = exchange.getRequest()
                .getHeaders()
                .getFirst("X-User-ID");

        String token = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        RegisterRequest registerRequest = getUserDetail(token);

        if (userId == null) {
            userId = registerRequest.getKeycloakId();
        }

        if (userId == null) {
            return chain.filter(exchange);
        }

        String finalUserId = userId;

        return userService.validateUser(finalUserId)
                .flatMap(exists -> {

                    if (exists) {
                        log.info("User already exists: {}", finalUserId);
                        return Mono.empty();
                    }

                    log.info("User does not exist. Registering: {}", finalUserId);

                    return userService.registerUser(registerRequest)
                            .then();
                })
                .then(Mono.defer(() -> {

                    ServerHttpRequest mutatedRequest =
                            exchange.getRequest()
                                    .mutate()
                                    .header("X-User-ID", finalUserId)
                                    .build();

                    ServerWebExchange mutatedExchange =
                            exchange.mutate()
                                    .request(mutatedRequest)
                                    .build();

                    return chain.filter(mutatedExchange);
                }));
    }

    private RegisterRequest getUserDetail(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization token");
        }

        try {

            String tokenWithoutBearer =
                    token.substring(7).trim();

            SignedJWT signedJWT =
                    SignedJWT.parse(tokenWithoutBearer);

            JWTClaimsSet claimsSet =
                    signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest =
                    new RegisterRequest();

            registerRequest.setEmail(
                    claimsSet.getStringClaim("email")
            );

            registerRequest.setKeycloakId(
                    claimsSet.getStringClaim("sub")
            );

            registerRequest.setFirstName(
                    claimsSet.getStringClaim("given_name")
            );

            registerRequest.setLastName(
                    claimsSet.getStringClaim("family_name")
            );

            registerRequest.setPassword("dummy@123");

            return registerRequest;

        } catch (ParseException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}