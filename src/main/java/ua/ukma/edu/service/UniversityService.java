package ua.ukma.edu.service;

import ua.ukma.edu.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UniversityService {
    private University University;

    public UniversityService(University university) {
        this.University = university;
    }

    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList<>();
        if (University.getFaculties() != null) {
            for (Faculty faculty : University.getFaculties()) {
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

    public List<Student> findStudentByLastName(String lastName) {
        List<Student> results = new ArrayList<>();
        if (lastName == null || lastName.trim().isEmpty()) {
            return results;
        }
        String searchFor = lastName.toLowerCase().trim();
        for (Student s : getAllStudents()) {
            if (s.getLastName().toLowerCase().contains(searchFor)) {
                results.add(s);
            }
        }
        return results;
    }

    public List<Student> getStudentsSortedByYear(){
        List<Student> sortedList = new ArrayList<>(getAllStudents());
        int n = sortedList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++){
                Student s1 = sortedList.get(j);
                Student s2 = sortedList.get(j+1);
                //міняє місцями якщо с1 старший за с2
                if(s1.getStudyYear() > s2.getStudyYear()){
                    sortedList.set(j, s2);
                    sortedList.set(j+1, s1);
                }
            }
        }
        return sortedList;
    }
}
