package ua.ukma.edu.service;

import ua.ukma.edu.domain.Student;
import ua.ukma.edu.domain.StudentStatus;
import ua.ukma.edu.dto.StudentDTO;
import ua.ukma.edu.dto.StudentMapper;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentService {
    private final Repository<Student, String> studentRepository;

    public StudentService(Repository<Student, String> studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findOptionalById(String id) {
        String normalizedId = validateId(id);
        return studentRepository.findById(normalizedId);
    }

    public Student findById(String id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID: " + id + " не знайдено."));
    }

    public void saveStudent(Student student) {
        validateStudent(student);
        studentRepository.save(student);
    }

    public void deleteStudentById(String id) {
        String normalizedId = validateId(id);
        studentRepository.deleteById(normalizedId);
    }

    public List<StudentDTO> findStudentByLnFnMn(String value){
        if (value == null || value.trim().isEmpty()) return new ArrayList<>();
        String searchFor = value.toLowerCase(Locale.ROOT).trim();

        return studentRepository.findAll().stream()
                .filter(student ->
                        (student.getFirstName() != null && student.getFirstName().toLowerCase(Locale.ROOT).contains(searchFor)) ||
                        (student.getLastName() != null && student.getLastName().toLowerCase(Locale.ROOT).contains(searchFor)) ||
                        (student.getPatronymic() != null && student.getPatronymic().toLowerCase(Locale.ROOT).contains(searchFor))
                )
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> getStudentsSortedByYear(){
        return studentRepository.findAll().stream()
                .sorted((s1, s2) -> Integer.compare(s1.getStudyYear(), s2.getStudyYear()))
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> findStudentByCourse(int course){
        return studentRepository.findAll().stream()
                .filter(student -> student.getStudyYear() == course)
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> findStudentByGroup(String group){
        if (group == null || group.trim().isEmpty()) return new ArrayList<>();
        String searchFor = group.toLowerCase(Locale.ROOT).trim();

        return studentRepository.findAll().stream()
                .filter(student -> student.getGroup() != null && student.getGroup().toLowerCase(Locale.ROOT).contains(searchFor))
                .map(StudentMapper::toDTO)
                .toList();
    }
    public Map<Integer, List<StudentDTO>> getStudentsGroupedByYear(){
        return studentRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Student::getStudyYear,
                        Collectors.mapping(StudentMapper::toDTO, Collectors.toList())
                ));
    }

    public Map<StudentStatus, List<StudentDTO>> getStudentsGroupedByStatus(){
        return studentRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Student::getStudentStatus,
                        Collectors.mapping(StudentMapper::toDTO, Collectors.toList())
                ));
    }

    private String validateId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID студента не може бути порожнім.");
        return id.trim();
    }

    private void validateStudent(Student student) {
        Objects.requireNonNull(student, "Студент не може бути null.");
        validateId(student.getId());

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("Ім'я студента не може бути порожнім.");
        if (student.getLastName() == null || student.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("Прізвище студента не може бути порожнім.");
        if (student.getGroup() == null || student.getGroup().trim().isEmpty())
            throw new IllegalArgumentException("Група студента не може бути порожною.");
        if (student.getStudyYear() <= 0 || student.getStudyYear() > 6)
            throw new IllegalArgumentException("Курс навчання повинен бути від 1 до 6.");
        if (student.getStudyForm() == null)
            throw new IllegalArgumentException("Форма навчання не може бути null.");
        if (student.getStudentStatus() == null)
            throw new IllegalArgumentException("Статус студента не може бути null.");
    }
}