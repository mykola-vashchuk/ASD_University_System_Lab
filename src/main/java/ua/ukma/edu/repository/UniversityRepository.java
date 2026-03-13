package ua.ukma.edu.repository;

import ua.ukma.edu.domain.University;

public class UniversityRepository extends InMemoryRepository<University, String> {

    @Override
    protected String getId(University entity) {
        return entity.getFullName(); // унікальність за повною назвою університету
    }
}