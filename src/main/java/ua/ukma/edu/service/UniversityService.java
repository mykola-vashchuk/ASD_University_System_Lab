package ua.ukma.edu.service;

import ua.ukma.edu.domain.*;
import java.util.ArrayList;
import java.util.List;

public class UniversityService {
    private University university;

    public UniversityService(University university) {
        this.university = university;
    }

    //збирає всіх студентів з усіх кафедр в один список
    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList<>();
        if (university.getFaculties() != null) {
            for (Faculty faculty : university.getFaculties()) {
                if (faculty.getDepartments() != null) {
                    for (Department department : faculty.getDepartments()) {
                        if (department.getStudents() != null) {
                            allStudents.addAll(department.getStudents());
                        }
                    }
                }
            }
        }
        return allStudents;
    }

    // 1. Пошук за Прізвищем
    public List<Student> findStudentByLastName(String lastName) {
        List<Student> results = new ArrayList<>();
        if (lastName == null || lastName.trim().isEmpty()) return results;

        String searchFor = lastName.toLowerCase().trim();
        for (Student s : getAllStudents()) {
            if (s.getLastName().toLowerCase().contains(searchFor)) {
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