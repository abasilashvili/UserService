package com.abasilashvili.user_service.service.validatortests;

import com.abasilashvili.user_service.dto.event.EventDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.event.EventStatus;
import com.abasilashvili.user_service.entity.event.EventType;
import com.abasilashvili.user_service.exceptions.SkillDoesntExistException;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapperImpl;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.validator.event.EventServiceValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceValidationTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private SkillDtoMapperImpl skillDtoMapper;

    @InjectMocks
    private EventServiceValidation validator;

    private EventDto createValidDto() {
        return new EventDto(1L, "New Event",
                LocalDateTime.now().plusMonths(2),
                LocalDateTime.now().plusMonths(4),
                1L, "Description Of Event",
                List.of(new SkillDto(1L, "Java"), new SkillDto(2L, "Tests")),
                "New York", 5, EventType.PRESENTATION, EventStatus.PLANNED);
    }

    @Test
    public void testShouldThrowIfTitleIsEmpty() {
        assertThrows(ValidationException.class, () -> validator.validateTitle(""));
    }

    @Test
    public void testShouldPassIfTitleIsNotEmpty() {
        assertDoesNotThrow(() -> validator.validateTitle("New Event"));
    }

    @Test
    public void testShouldThrowIfStartDateIsNull() {
        assertThrows(ValidationException.class, () -> validator.validateStartDate(null));
    }

    @Test
    public void testShouldPassIfStartDateIsOk() {
        assertDoesNotThrow(() -> validator.validateStartDate(LocalDateTime.now()));
    }

    @Test
    public void testShouldThrowIfUserDoesNotExist() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> validator.validateOwner(1L));
    }

    @Test
    public void testShouldPassIfUserIsValid() {
        User mockUser = mock(User.class);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockUser));

        assertDoesNotThrow(() -> validator.validateOwner(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testValidateSkills_ShouldThrowWhenUserNotFound() {
        EventDto dto = createValidDto();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> validator.validateSkills(dto));

        verify(userRepository, times(1))
                .findById(dto.getOwnerId());
    }

    @Test
    public void testValidateSkills_ShouldThrowWhenSkillsNotMatch() {
        EventDto dto = createValidDto();
        User mockUser = mock(User.class);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockUser));
        when(mockUser.getSkills()).thenReturn(List.of());

        assertThrows(SkillDoesntExistException.class,
                () -> validator.validateSkills(dto));

        verify(userRepository, times(1)).findById(dto.getOwnerId());
    }

    @Test
    public void testValidateSkills_ShouldPassWhenSkillsMatch() {
        EventDto dto = createValidDto();
        User mockUser = mock(User.class);

        Skill javaSkill = Skill.builder().id(1L).title("Java").build();
        Skill testSkill = Skill.builder().id(2L).title("Tests").build();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockUser));

        when(mockUser.getSkills())
                .thenReturn(List.of(javaSkill, testSkill));

        assertDoesNotThrow(() -> validator.validateSkills(dto));
        verify(userRepository, times(1))
                .findById(dto.getOwnerId());
    }

    @Test
    public void testValidateSkills_ShouldPassWhenUserHasMoreSkills() {

        EventDto dto = createValidDto();
        User mockUser = mock(User.class);

        Skill javaSkill = Skill.builder().id(1L).title("Java").build();
        Skill testSkill = Skill.builder().id(2L).title("Tests").build();
        Skill springSkill = Skill.builder().id(3L).title("Spring").build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(mockUser.getSkills()).thenReturn(List.of(javaSkill, testSkill, springSkill));

        assertDoesNotThrow(() -> validator.validateSkills(dto));
        verify(userRepository, times(1)).findById(dto.getOwnerId());
    }

    @Test
    public void testValidateSkills_ShouldThrowWhenMissingOneSkill() {

        EventDto dto = createValidDto();
        User mockUser = mock(User.class);

        Skill javaSkill = Skill.builder().id(1L).title("Java").build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(mockUser.getSkills()).thenReturn(List.of(javaSkill));

        assertThrows(SkillDoesntExistException.class, () -> validator.validateSkills(dto));
        verify(userRepository, times(1)).findById(dto.getOwnerId());
    }
}
