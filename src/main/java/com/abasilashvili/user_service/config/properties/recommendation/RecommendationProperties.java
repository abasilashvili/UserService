package com.abasilashvili.user_service.config.properties.recommendation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "recommendation")
@Data
public class RecommendationProperties {
    private int cooldownMonths = 6;
    private int maxRequestsPerDay = 10;
}
