package com.abasilashvili.user_service.service.education;

import com.abasilashvili.user_service.dto.education.EducationDto;
import com.abasilashvili.user_service.entity.Education;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.education.EducationMapper;
import com.abasilashvili.user_service.repository.EducationRepository;
import com.abasilashvili.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EducationService {

    private final UserRepository userRepository;
    private final EducationMapper educationMapper;
    private final EducationRepository educationRepository;

    public EducationDto addEducation(Long userId, EducationDto dto) {
        if (checkYearFrom(dto.getYearFrom())) {
            throw new ValidationException("Year must be in past.");
        }
        User toUpdate = userRepository.findById(userId).get();

        Education newEdu = Education.builder()
                .educationLevel(dto.getEducationLevel())
                .yearFrom(dto.getYearFrom())
                .yearTo(dto.getYearTo())
                .institution(dto.getInstitution())
                .specialization(dto.getSpecialization())
                .user(toUpdate)
                .build();

        educationRepository.save(newEdu);


        toUpdate.getEducation().add(newEdu);
        userRepository.save(toUpdate);

        return educationMapper.toDto(newEdu);
    }

    public EducationDto updateEducation(Long userId, EducationDto dto) {
        if (checkYearFrom(dto.getYearFrom())) {
            throw new ValidationException("Year must be in past.");
        }

        User toUpdate = userRepository.findById(userId).get();
        Education eduToUpdate = toUpdate.getEducation().stream()
                .filter(edu -> edu.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("No education with this id to update."));

        eduToUpdate.setEducationLevel(dto.getEducationLevel());
        eduToUpdate.setSpecialization(dto.getSpecialization());
        eduToUpdate.setYearFrom(dto.getYearFrom());
        eduToUpdate.setYearTo(dto.getYearTo());

        toUpdate.getEducation().add(eduToUpdate);

        educationRepository.save(eduToUpdate);
        userRepository.save(toUpdate);

        return educationMapper.toDto(eduToUpdate);
    }

    public EducationDto getEducationById(Long eduId) {
        return educationMapper.toDto(educationRepository.findById(eduId)
                .orElseThrow(() -> new ValidationException("No education with such ID")));
    }

    private boolean checkYearFrom(Integer yearFrom) {
        return yearFrom >= LocalDateTime.now().getYear();
    }
}
