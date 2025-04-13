package com.abasilashvili.user_service.service.mappertests;

import com.abasilashvili.user_service.dto.recommendation.RecommendationDto;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.recommendation.Recommendation;
import com.abasilashvili.user_service.mappers.recommendation.RecommendationDtoMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RecommendationDtoMapperTest {

    private final RecommendationDtoMapper mapper = Mappers.getMapper(RecommendationDtoMapper.class);

    @Test
    void shouldMapEntityToDtoCorrectly() {

        User mockUser = mock(User.class);
        User mockUser2 = mock(User.class);
        Recommendation entity = Recommendation.builder()
                .id(1L)
                .author(mockUser)
                .content("Content")
                .receiver(mockUser2)
                .build();

        RecommendationDto dto = mapper.toDto(entity);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getContent(), entity.getContent());
    }
}
