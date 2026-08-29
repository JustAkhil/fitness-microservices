package com.fitness.aiservice.dto;

import lombok.Data;

@Data
public class RecommendationRequest {

    private String userId;
    private String activityId;
    private String recommendation;

}