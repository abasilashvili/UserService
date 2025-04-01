package com.abasilashvili.user_service.mappers.recommendation;

import com.abasilashvili.user_service.dto.recommendation.RecommendationDto;
import com.abasilashvili.user_service.entity.recommendation.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationDtoMapper {
    RecommendationDto toDto(Recommendation recommendation);
    Recommendation toEntity(RecommendationDto recommendationDto);
}
