package com.fitness.userservice.repositories;

import com.fitness.userservice.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    Boolean existsByEmail(String email);

    boolean existsByKeycloakId(String userId);

    UserModel findByEmail(String email);
}