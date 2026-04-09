package ua.ukma.edu.dto;

import ua.ukma.edu.domain.Student;

public class StudentMapper {
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getPatronymic(),
                student.getBirthDate(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getStudentId(),
                student.getGroup(),
                student.getStudyForm(),
                student.getStudentStatus(),
                student.getStudyYear(),
                student.getAdmissionYear()
        );
    }
}