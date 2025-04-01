package com.abasilashvili.user_service.mappers.recommendation;

import com.abasilashvili.user_service.dto.recommendation.RecommendationRequestDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.recommendation.RecommendationRequest;
import com.abasilashvili.user_service.entity.recommendation.SkillRequest;
import com.abasilashvili.user_service.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RecommendationRequestMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "receiverId", source = "receiver.id")
    public abstract RecommendationRequestDto toDto(RecommendationRequest recommendationRequest);

    @Mapping(target = "requester", expression = "java(getUser(recommendationRequestDto.requesterId()))")
    @Mapping(target = "receiver", expression = "java(getUser(recommendationRequestDto.receiverId()))")
    public abstract RecommendationRequest toEntity(RecommendationRequestDto recommendationRequestDto);

    protected User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    List<SkillDto> mapSkills(List<SkillRequest> skillRequests) {
        return skillRequests.stream()
                .map(skillRequest -> new SkillDto(
                        skillRequest.getSkill().getId(),
                        skillRequest.getSkill().getTitle()
                ))
                .toList();
    }
}
