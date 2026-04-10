package ua.ukma.edu.service;

import ua.ukma.edu.domain.Department;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class DepartmentService {
    private final Repository<Department, String> departmentRepository;

    public DepartmentService(Repository<Department, String> departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findOptionalById(String id) {
        String normalizedId = validateId(id);
        return departmentRepository.findById(normalizedId);
    }

    public Department findById(String id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кафедру з ID: " + id + " не знайдено."));
    }

    public List<Department> findDepartmentByName(String query) {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();
        String searchFor = query.toLowerCase(Locale.ROOT).trim();

        return getAllDepartments().stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase(Locale.ROOT).contains(searchFor))
                .toList();
    }

    public List<Department> getDepartmentsSortedAlphabetically() {
        Comparator<String> stringComparator = Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);

        return getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getName, stringComparator))
                .toList();
    }

    public void saveDepartment(Department department) {
        validateDepartment(department);
        departmentRepository.save(department);
    }

    public Department updateDepartment(Department department) {
        validateDepartment(department);
        return departmentRepository.update(department);
    }

    public void deleteDepartmentById(String id) {
        String normalizedId = validateId(id);
        departmentRepository.deleteById(normalizedId);
    }

    private String validateId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID кафедри не може бути порожнім.");
        return id.trim();
    }

    private void validateDepartment(Department department) {
        Objects.requireNonNull(department, "Кафедра не може бути null.");
        validateId(department.getId());

        if (department.getName() == null || department.getName().trim().isEmpty())
            throw new IllegalArgumentException("Назва кафедри не може бути порожною.");
        if (department.getLocation() == null || department.getLocation().trim().isEmpty())
            throw new IllegalArgumentException("Локація кафедри не може бути порожною.");
    }
}

