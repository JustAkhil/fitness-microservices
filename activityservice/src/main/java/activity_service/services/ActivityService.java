package activity_service.services;

import activity_service.dto.ActivityRequest;
import activity_service.dto.ActivityResponse;
import activity_service.model.Activity;
import activity_service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserValidationService userValidationService;
    private final ActivityRepository activityRepository;
    public ActivityResponse trackActivity(ActivityRequest request) {
        boolean isValid=userValidationService.validateUser(request.getUserId());
        if(!isValid){
            throw new RuntimeException("Invalid user id: "+request.getUserId());
        }
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .activityType(request.getActivityType())
                .additionalMetrics(request.getAdditionalMetrics())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .build();
        Activity savedActivity=activityRepository.save(activity);
        return mapToActivityResponse(savedActivity);
    }

    private ActivityResponse mapToActivityResponse(Activity savedActivity) {
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setActivityId(savedActivity.getActivityId());
        activityResponse.setUserId(savedActivity.getUserId());
        activityResponse.setActivityType(savedActivity.getActivityType());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setCaloriesBurned(savedActivity.getCaloriesBurned());
        activityResponse.setStartTime(savedActivity.getStartTime());
        activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        return activityResponse;
    }
}
