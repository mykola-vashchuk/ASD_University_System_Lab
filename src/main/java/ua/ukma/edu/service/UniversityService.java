package ua.ukma.edu.service;

import ua.ukma.edu.domain.Faculty;
import ua.ukma.edu.domain.University;
import ua.ukma.edu.exception.EntityNotFoundException;

import java.util.List;

public class UniversityService {

    private final University university;

    public UniversityService(University university) {
        this.university = university;
    }

    public University getUniversity() {
        return university;
    }

    public String getFullName() {
        return university.getShortName();
    }

    public String getShortName() {
        return university.getShortName();
    }

    public List<Faculty> getAllFaculties() {
        return university.getFaculties();
    }

    public Faculty findFacultyByShortName(String shortName) {
        if (shortName == null || shortName.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва факультету не може бути порожньою.");
        }
        return university.getFaculties().stream()
                .filter(f -> f.getShortName().equalsIgnoreCase(shortName.trim()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Факультет '" + shortName + "' не знайдено."));
    }
}