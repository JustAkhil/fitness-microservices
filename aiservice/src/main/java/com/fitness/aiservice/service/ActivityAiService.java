package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
    private final GraQService graQService;

    public void generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        log.info("Response from Ai:{}", graQService.getRecommendation(prompt));
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        You are an expert fitness activity analysis assistant.
                        
                        Your task is to analyze the fitness activity provided below and return a
                        clear, accurate, practical fitness analysis.
                        
                        IMPORTANT RULES:
                        1. Return ONLY valid JSON.
                        2. Do NOT use Markdown or code fences.
                        3. Do NOT add any text before or after the JSON.
                        4. Follow the JSON structure EXACTLY as provided.
                        5. Do NOT add, remove, rename, or change any JSON fields.
                        6. Every value must contain useful and specific information.
                        7. Base your analysis ONLY on the data provided.
                        8. NEVER invent missing information such as age, gender, body weight,
                           maximum heart rate, fitness level, medical history, or training goals.
                        9. If a metric is unavailable, do not make assumptions about it.
                        10. Do not make medical diagnoses.
                        11. Recommendations should be realistic, actionable, and appropriate for
                            the activity and measured performance.
                        12. Avoid generic statements when the provided metrics allow a more specific
                            recommendation.
                        
                        CALCULATION RULES:
                        - If distance and duration are available, calculate average pace correctly:
                          pace = duration / distance.
                        - If distance and duration are available, calculate average speed correctly:
                          speed = distance / duration.
                        - Do NOT confuse pace (min/km) with speed (km/h).
                        - Only calculate heart-rate zones if maximum heart rate, age, or sufficient
                          heart-rate information is explicitly provided.
                        - Do NOT describe calories as high or low unless enough information is
                          available to make a meaningful comparison.
                        - If additional metrics contain steps, cadence, distance, heart rate, pace,
                          speed, elevation, or other values, use them when relevant.
                        - Clearly distinguish measured values from calculated values.
                        
                        ANALYSIS REQUIREMENTS:
                        
                        1. overall:
                           Give a concise summary of the complete activity.
                           Mention the activity type, duration, major performance metrics, and
                           overall training quality when supported by the data.
                        
                        2. pace:
                           Analyze pace or speed when distance and duration are available.
                           If pace cannot be calculated, explain that the available data is
                           insufficient to evaluate pace.
                           Do not invent a pace.
                        
                        3. heartRate:
                           Analyze heart rate only when heart-rate data is available.
                           Discuss intensity using only information supported by the provided data.
                           Do not assign heart-rate zones based on an assumed age or maximum heart rate.
                        
                        4. caloriesBurned:
                           Discuss the recorded calorie expenditure.
                           Do not assume body weight or claim that the calorie value is unusually
                           high or low without sufficient information.
                        
                        5. improvements:
                           Provide 3 to 5 specific improvements.
                           Each improvement must have:
                           - a clear area
                           - a practical recommendation
                           Recommendations should directly relate to the activity data.
                        
                        6. suggestions:
                           Provide 2 to 4 suitable next-workout suggestions.
                           Each workout must include:
                           - workout name
                           - detailed description
                           Include duration, intensity, repetitions, recovery, or progression
                           when appropriate.
                           Do not recommend advanced training without evidence that it is suitable.
                        
                        7. safety:
                           Provide 2 to 4 relevant safety guidelines.
                           Include warm-up, recovery, hydration, gradual progression, or warning
                           signs when relevant.
                           Keep safety advice general and do not provide medical diagnosis.
                        
                        EXPECTED JSON FORMAT:
                        {
                          "analysis": {
                            "overall": "Overall analysis here",
                            "pace": "Pace analysis here",
                            "heartRate": "Heart rate analysis here",
                            "caloriesBurned": "Calories analysis here"
                          },
                          "improvements": [
                            {
                              "area": "Area name",
                              "recommendation": "Detailed recommendation"
                            }
                          ],
                          "suggestions": [
                            {
                              "workout": "Workout name",
                              "description": "Detailed workout description"
                            }
                          ],
                          "safety": [
                            "Safety point 1",
                            "Safety point 2"
                          ]
                        }
                        
                        ACTIVITY DATA:
                        Activity Type: %s
                        Duration: %d minutes
                        Calories Burned: %d
                        Additional Metrics: %s
                        
                        FINAL INSTRUCTION:
                        Analyze the activity using the rules above and return ONLY the valid JSON
                        object matching the exact schema. Do not include explanations outside JSON.
                        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
