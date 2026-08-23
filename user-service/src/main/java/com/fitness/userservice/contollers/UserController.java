package com.fitness.userservice.contollers;


import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse>register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>getUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
