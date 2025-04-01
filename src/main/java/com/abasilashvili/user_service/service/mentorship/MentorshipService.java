package com.abasilashvili.user_service.service.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MenteeDto;
import com.abasilashvili.user_service.dto.mentorship.MentorDto;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.mappers.mentorship.MenteeDtoMapper;
import com.abasilashvili.user_service.mappers.mentorship.MentorDtoMapper;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.mentorship.MentorshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    private final UserRepository userRepository;
    private final MenteeDtoMapper menteeDtoMapper;
    private final MentorDtoMapper mentorDtoMapper;

    public List<MenteeDto> getMentees(Long id) {
        log.info("Received request to get all mentees for user with id {}. Service.", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует."));

        return user.getMentees().stream()
                .map(menteeDtoMapper::toDto)
                .toList();
    }

    public List<MentorDto> getMentors(Long id) {
        log.info("Received request to get all mentors for user with id {}. Service.", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует."));

        return user.getMentors().stream()
                .map(mentorDtoMapper::toDto)
                .toList();
    }

    public ResponseEntity<String> deleteMentee(Long menteeId, Long mentorId) {
        log.info("Received request to delete mentee from mentor {}. Service", mentorId);
        User user = userRepository.findById(mentorId)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существоует"));

        List<User> menteesList = user.getMentees();
        for (User mentee : menteesList) {
            if (Objects.equals(mentee.getId(), menteeId)) {
                menteesList.remove(mentee);
                log.info("Removed mentee {}", menteeId);
                user.setMentees(menteesList);
                userRepository.save(user);
                break;
            }
        }
        return ResponseEntity.ok("Mentee deleted.");
    }

    public ResponseEntity<String> deleteMentor(Long menteeId, Long mentorId) {
        log.info("Received request to delete mentor from mentee {}. Controller", menteeId);
        User user = userRepository.findById(menteeId)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существоует"));

        List<User> mentorList = user.getMentors();
        for (User mentor : mentorList) {
            if (Objects.equals(mentor.getId(), mentorId)) {
                mentorList.remove(mentor);
                log.info("Removed mentor {}", mentorId);
                user.setMentors(mentorList);
                userRepository.save(user);
                break;
            }
        }
        return ResponseEntity.ok("Mentor deleted.");
    }
}
