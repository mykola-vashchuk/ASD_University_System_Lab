package ua.ukma.edu.dto;

import ua.ukma.edu.domain.StudentStatus;
import ua.ukma.edu.domain.StudyForm;

import java.time.LocalDate;

public record StudentDTO (
       String id,
       String firstName,
       String lastName,
       String patronymic,
       LocalDate birthDate,
       String email,
       String phoneNumber,

       String studentId,
       String group,
       StudyForm studyForm,
       StudentStatus studentStatus,
       int studyYear,
       int admissionYear
){
}