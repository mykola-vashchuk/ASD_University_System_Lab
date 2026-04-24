package ua.ukma.edu.service;

import ua.ukma.edu.domain.Teacher;
import ua.ukma.edu.domain.Position;
import ua.ukma.edu.domain.ScientificDegree;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeacherService {
    private final Repository<Teacher, String> teacherRepository;

    public TeacherService(Repository<Teacher, String> teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findOptionalById(String id) {
        String normalizedId = validateId(id);
        return teacherRepository.findById(normalizedId);
    }

    public Teacher findById(String id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Викладача з ID: " + id + " не знайдено."));
    }

    public List<Teacher> findTeacherByLnFnMn(String query) {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();
        String searchFor = query.toLowerCase(Locale.ROOT).trim();

        return getAllTeachers().stream()
                .filter(t -> (t.getLastName() != null && t.getLastName().toLowerCase(Locale.ROOT).contains(searchFor)) ||
                        (t.getFirstName() != null && t.getFirstName().toLowerCase(Locale.ROOT).contains(searchFor)) ||
                        (t.getPatronymic() != null && t.getPatronymic().toLowerCase(Locale.ROOT).contains(searchFor)))
                .toList();
    }

    public List<Teacher> getTeachersSortedAlphabetically() {
        Comparator<String> stringComparator = Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);

        return getAllTeachers().stream()
                .sorted(Comparator.comparing(Teacher::getLastName, stringComparator)
                        .thenComparing(Teacher::getFirstName, stringComparator))
                .toList();
    }

    public void saveTeacher(Teacher teacher) {
        validateTeacher(teacher);
        teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Teacher teacher) {
        validateTeacher(teacher);
        return teacherRepository.update(teacher);
    }

    public void deleteTeacherById(String id) {
        String normalizedId = validateId(id);
        teacherRepository.deleteById(normalizedId);
    }

    public Map<Position, List<Teacher>> getTeachersGroupedByPosition(){
        return getAllTeachers().stream()
                .collect(Collectors.groupingBy(
                        Teacher::getPosition,
                        Collectors.toList()
                ));
    }

    public Map<ScientificDegree, List<Teacher>> getTeachersGroupedByDegree(){
        return getAllTeachers().stream()
                .collect(Collectors.groupingBy(
                        Teacher::getDegree,
                        Collectors.toList()
                ));
    }

    private String validateId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID викладача не може бути порожнім.");
        return id.trim();
    }

    private void validateTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher, "Викладач не може бути null.");
        validateId(teacher.getId());

        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("Ім'я викладача не може бути порожнім.");
        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("Прізвище викладача не може бути порожнім.");
    }
}
