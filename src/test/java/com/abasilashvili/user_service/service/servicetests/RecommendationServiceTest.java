package com.abasilashvili.user_service.service.servicetests;

import com.abasilashvili.user_service.dto.recommendation.RecommendationDto;
import com.abasilashvili.user_service.dto.recommendation.SkillOfferDto;
import com.abasilashvili.user_service.exceptions.AlreadyExistsException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.recommendation.RecommendationDtoMapper;
import com.abasilashvili.user_service.repository.recommendation.RecommendationRepository;
import com.abasilashvili.user_service.repository.recommendation.SkillOfferRepository;
import com.abasilashvili.user_service.service.recommendation.RecommendationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private SkillOfferRepository skillOfferRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private RecommendationDto createValidDto() {
        return new RecommendationDto(1L, 1L, 2L,
                "Recommendation content",
                List.of(new SkillOfferDto(1L, 1L),
                        new SkillOfferDto(2L, 2L)),
                LocalDateTime.now().minusMonths(6));
    }

    @Test
    public void giveRecommendation_ShouldCreateRecommendationAndSkillOffers() {
        RecommendationDto validDto = createValidDto();
        when(recommendationRepository.create(
                eq(validDto.getAuthorId()),
                eq(validDto.getReceiverId()),
                eq(validDto.getContent())))
                .thenReturn(1L);

        recommendationService.giveRecommendation(validDto);

        verify(recommendationRepository, times(1))
                .create(validDto.getAuthorId(), validDto.getReceiverId(), validDto.getContent());

        verify(skillOfferRepository, times(validDto.getSkillOffers().size()))
                .create(anyLong(), eq(1L));

        List<SkillOfferDto> skillOffers = validDto.getSkillOffers();

        for (SkillOfferDto skillOffer : skillOffers) {
            verify(skillOfferRepository, atLeastOnce())
                    .create(skillOffer.getId(), 1L);
        }
    }

    @Test
    public void giveRecommendation_ShouldThrowWhenAuthorEqualsReceiver() {
        RecommendationDto invalidDto = new RecommendationDto(
                1L,
                1L,
                1L,
                "Valid content",
                List.of(new SkillOfferDto(1L, 1L)),
                LocalDateTime.now().minusMonths(7)
        );

        assertThrows(AlreadyExistsException.class,
                () -> recommendationService.giveRecommendation(invalidDto),
                "Ожидалось исключение из-за совпадения authorId и receiverId");

        verify(recommendationRepository, never())
                .create(anyLong(), anyLong(), any());

        verify(skillOfferRepository, never())
                .create(anyLong(), anyLong());
    }

    @Test
    public void giveRecommendation_ShouldThrowWhenCooldownPeriodDidNotPass() {
        RecommendationDto invalidDto = new RecommendationDto(
                1L,
                1L,
                2L,
                "Valid content",
                List.of(new SkillOfferDto(1L, 1L)),
                LocalDateTime.now().minusMonths(5)
        );

        assertThrows(AlreadyExistsException.class,
                () -> recommendationService.giveRecommendation(invalidDto));

        verify(recommendationRepository, never())
                .create(anyLong(), anyLong(), any());

        verify(skillOfferRepository, never())
                .create(anyLong(), anyLong());
    }

    @Test
    public void giveRecommendation_ShouldThrowIfNullSkills() {
        RecommendationDto invalidDto = new RecommendationDto(
                1L,
                1L,
                2L,
                "Valid content",
                List.of(new SkillOfferDto(null, null)),
                LocalDateTime.now().minusMonths(7)
        );

        assertThrows(ValidationException.class,
                () -> recommendationService.giveRecommendation(invalidDto));

        verify(recommendationRepository, never())
                .create(anyLong(), anyLong(), any());

        verify(skillOfferRepository, never())
                .create(anyLong(), anyLong());
    }

    @Test
    public void updateRecommendation_ShouldUpdateAndReplaceSkillOffers() {
        RecommendationDto validDto = createValidDto();
        validDto.setContent("Updated Content");
        validDto.setSkillOffers(List.of(
                new SkillOfferDto(3L, validDto.getId()),
                new SkillOfferDto(4L, validDto.getId())
        ));

        recommendationService.updateRecommendation(validDto);

        assertEquals(1L, validDto.getAuthorId(), "AuthorId не должен меняться");
        assertEquals(2L, validDto.getReceiverId(), "ReceiverId не должен меняться");

        verify(recommendationRepository, times(1))
                .update(validDto.getAuthorId(), validDto.getReceiverId(), validDto.getContent());

        verify(skillOfferRepository, times(1))
                .deleteAllByRecommendationId(validDto.getId());

        verify(skillOfferRepository, times(validDto.getSkillOffers().size()))
                .create(anyLong(), eq(validDto.getId()));
    }

    @Test
    public void updateRecommendation_ShouldThrowWhenAuthorEqualsReceiver() {
        RecommendationDto invalidDto = createValidDto();
        invalidDto.setReceiverId(invalidDto.getAuthorId());

        assertThrows(AlreadyExistsException.class,
                () -> recommendationService.updateRecommendation(invalidDto));

        verify(recommendationRepository, never())
                .update(anyLong(), anyLong(), anyString());
        verify(skillOfferRepository, never()).deleteAllByRecommendationId(anyLong());
        verify(skillOfferRepository, never()).create(anyLong(), anyLong());
    }

    @Test
    public void updateRecommendation_ShouldThrowWhenEmptyContent() {
        RecommendationDto invalidDto = createValidDto();
        invalidDto.setContent("");

        assertThrows(ValidationException.class,
                () -> recommendationService.updateRecommendation(invalidDto));

        verify(recommendationRepository, never())
                .update(anyLong(), anyLong(), anyString());
        verify(skillOfferRepository, never()).deleteAllByRecommendationId(anyLong());
        verify(skillOfferRepository, never()).create(anyLong(), anyLong());
    }

    @Test
    public void deleteRecommendation_ShouldCallRepositoryDelete() {
        Long recommendationId = 1L;

        recommendationService.deleteRecommendation(recommendationId);

        verify(recommendationRepository, times(1))
                .deleteById(eq(recommendationId));

        verifyNoMoreInteractions(recommendationRepository, skillOfferRepository);
    }
}
