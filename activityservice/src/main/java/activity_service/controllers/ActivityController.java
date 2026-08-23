package activity_service.controllers;

import activity_service.dto.ActivityRequest;
import activity_service.dto.ActivityResponse;
import activity_service.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    private ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

}
