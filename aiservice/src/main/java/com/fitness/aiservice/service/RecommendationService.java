package com.fitness.aiservice.service;

import com.fitness.aiservice.dto.RecommendationResponse;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;


    private RecommendationResponse mapToRecommendationResponse(
            Recommendation recommendation) {

        return RecommendationResponse.builder()
                .recommendationId(recommendation.getRecommendationId())
                .userId(recommendation.getUserId())
                .activityId(recommendation.getActivityId())
                .recommendation(recommendation.getRecommendation())
                .improvements(recommendation.getImprovements())
                .suggestions(recommendation.getSuggestions())
                .safety(recommendation.getSafety())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }

    public List<RecommendationResponse> getUserRecommendations(String userId) {
        List<Recommendation> responsesList = recommendationRepository.findByUserId(userId);
        return responsesList.stream()
                .map(r -> mapToRecommendationResponse(r))
                .toList();
    }

    public RecommendationResponse getActivityRecommendation(String activityId) {
        return mapToRecommendationResponse(recommendationRepository.findByActivityId(activityId)
                .orElseThrow(()->new RuntimeException("Activity not found!: "+activityId))
        );
    }
}
