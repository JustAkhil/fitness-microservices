package activity_service.dto;

import activity_service.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String activityId;
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
