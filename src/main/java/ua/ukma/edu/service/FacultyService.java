package ua.ukma.edu.service;

import ua.ukma.edu.domain.Faculty;
import ua.ukma.edu.domain.Teacher;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.*;

public class FacultyService {
    private final Repository<Faculty, String> facultyRepository;

    public FacultyService(Repository<Faculty, String> facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> getAllFaculties(){
        return facultyRepository.findAll();
    }

    public Optional<Faculty> findOptionalById(String id){
        String normalizedId = validateId(id);
        return facultyRepository.findById(normalizedId);
    }

    public Faculty findById(String id){
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Факультету з ID: " + id + " не знайдено."));
    }

    public List<Faculty> findFacultyByShortOrFullName(String querry){
        if (querry == null || querry.trim().isEmpty()) return new ArrayList<>();
        String searchFor = querry.toLowerCase(Locale.ROOT).trim();

        return getAllFaculties().stream()
                .filter(f -> (f.getFullName() != null && f.getFullName().toLowerCase(Locale.ROOT).contains(searchFor)) ||
                        (f.getShortName() != null && f.getShortName().toLowerCase(Locale.ROOT).contains(searchFor)))
                .toList();
    }

    public List<Faculty> getFacultiesSortedAlphabetically(){
        Comparator<String> stringComparator = Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);

        return getAllFaculties().stream()
                .sorted(Comparator.comparing(Faculty::getFullName, stringComparator)
                        .thenComparing(Faculty::getShortName, stringComparator)
                )
                .toList();
    }

    public void saveFaculty(Faculty faculty){
        validateFaculty(faculty);
        facultyRepository.save(faculty);
    }

    public void updateFaculty(Faculty faculty){
        validateFaculty(faculty);
        facultyRepository.update(faculty);
    }

    public void deleteFacultyById(String id){
        String normalizedId = validateId(id);
        facultyRepository.deleteById(normalizedId);
    }

    private String validateId(String id){
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID факультету не може бути порожнім.");
        return id.trim();
    }

    private void validateFaculty(Faculty faculty){
        Objects.requireNonNull(faculty, "Факултет не може бути null.");
        validateId(faculty.getId());

        if(faculty.getFullName() == null || faculty.getFullName().trim().isEmpty())
            throw new IllegalArgumentException("Повне ім'я факультету не може бути порожнім.");
        if(faculty.getShortName() == null || faculty.getShortName().trim().isEmpty())
            throw new IllegalArgumentException("Абревіатура факультету не може бути порожньою.");
    }
}
