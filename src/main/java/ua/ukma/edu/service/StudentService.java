package ua.ukma.edu.service;

import ua.ukma.edu.domain.Student;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.*;

public class StudentService {
    private final Repository<Student, String> studentRepository;

    public StudentService(Repository<Student, String> studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findOptionalById(String id) {
        return studentRepository.findById(id);
    }

    public Student findById(String id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID: " + id + " не знайдено."));
    }

    public List<Student> findStudentByLnFnMn(String query) {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();
        String searchFor = query.toLowerCase().trim();

        return getAllStudents().stream()
                .filter(s -> s.getLastName().toLowerCase(Locale.ROOT).contains(searchFor) ||
                        s.getFirstName().toLowerCase(Locale.ROOT).contains(searchFor) ||
                        s.getPatronymic().toLowerCase(Locale.ROOT).contains(searchFor))
                .toList();
    }

    public List<Student> findStudentByCourse(int course) {
        return getAllStudents().stream()
                .filter(s -> s.getStudyYear() == course)
                .toList();
    }

    public List<Student> findStudentByGroup(String group) {
        if (group == null || group.trim().isEmpty()) return new ArrayList<>();

        return getAllStudents().stream()
                .filter(s -> s.getGroup().equalsIgnoreCase(group.trim()))
                .toList();
    }

    public List<Student> getStudentsSortedByYear() {
        return getAllStudents().stream()
                .sorted(Comparator.comparingInt(Student::getStudyYear))
                .toList();
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(String id) {
        studentRepository.deleteById(id);
    }

}