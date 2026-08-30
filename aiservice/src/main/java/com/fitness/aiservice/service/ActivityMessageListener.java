package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @KafkaListener(topics = "${kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void processActivity(Activity activity){
        log.info("Receive Activity for processing: {}",activity.getUserId());
    }
}
