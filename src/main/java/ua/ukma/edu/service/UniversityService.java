package ua.ukma.edu.service;

import ua.ukma.edu.domain.*;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class UniversityService {
    private final University university;
    private final Repository<Student, String> studentRepository;

    public UniversityService(University university, Repository<Student, String> studentRepository) {
        this.university = university;
        this.studentRepository = studentRepository;
    }

    //
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student findById(String id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID: " + id +" не знайдено."));
    }

    // 1. Пошук за Прізвищем
    public List<Student> findStudentByLnFnMn(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return results;

        String searchFor = query.toLowerCase().trim();
        for (Student s : getAllStudents()) {
            boolean matchLast = s.getLastName().toLowerCase().contains(searchFor);
            boolean matchFirst = s.getFirstName().toLowerCase().contains(searchFor);
            boolean matchPatronymic = s.getPatronymic().toLowerCase().contains(searchFor);
            if ( matchLast || matchFirst || matchPatronymic) {
                results.add(s);
            }
        }
        return results;
    }

    // 2. Пошук за Курсом
    public List<Student> findStudentByCourse(int course) {
        List<Student> results = new ArrayList<>();
        for (Student s : getAllStudents()) {
            if (s.getStudyYear() == course) {
                results.add(s);
            }
        }
        return results;
    }

    // 3. Пошук за Групою
    public List<Student> findStudentByGroup(String group) {
        List<Student> results = new ArrayList<>();
        if (group == null) return results;

        for (Student s : getAllStudents()) {
            if (s.getGroup().equalsIgnoreCase(group.trim())) {
                results.add(s);
            }
        }
        return results;
    }

    // 4. Сортування
    public List<Student> getStudentsSortedByYear() {
        List<Student> sortedList = new ArrayList<>(getAllStudents());
        int n = sortedList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Student s1 = sortedList.get(j);
                Student s2 = sortedList.get(j + 1);
                if (s1.getStudyYear() > s2.getStudyYear()) {
                    sortedList.set(j, s2);
                    sortedList.set(j + 1, s1);
                }
            }
        }
        return sortedList;
    }
}