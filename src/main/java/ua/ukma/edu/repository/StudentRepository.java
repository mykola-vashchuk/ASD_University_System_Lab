package ua.ukma.edu.repository;

import ua.ukma.edu.domain.Student;

import java.util.*;

public class StudentRepository implements Repository<Student, String>{

    private final Map<String, Student> storage  = new HashMap<>();

    @Override
    public void save(Student entity) {
        storage.put(entity.getStudentId(), entity);
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}
