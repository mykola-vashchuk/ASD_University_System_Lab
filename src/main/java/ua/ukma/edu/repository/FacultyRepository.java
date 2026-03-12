package ua.ukma.edu.repository;

import ua.ukma.edu.domain.Faculty;

public class FacultyRepository extends InMemoryRepository<Faculty, String> {
    @Override
    protected String getId(Faculty entity) { return entity.getId(); }
}