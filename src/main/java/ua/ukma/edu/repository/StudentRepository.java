package ua.ukma.edu.repository;

import ua.ukma.edu.domain.Student;
import java.util.*;

public class StudentRepository extends InMemoryRepository<Student, String> {

    @Override
    protected String getId(Student entity) { return entity.getId(); }

    public List<Student> findByGroup(String group) {
        List<Student> result = new ArrayList<>();
        for (Student s : storage.values()) if (s.getGroup().equalsIgnoreCase(group)) result.add(s);
        return result;
    }

    public List<Student> findByCourse(int course) {
        List<Student> result = new ArrayList<>();
        for (Student s : storage.values()) if (s.getStudyYear() == course) result.add(s);
        return result;
    }

    public List<Student> sortedByCourseThenName() {
        List<Student> result = new ArrayList<>(storage.values());
        result.sort((a, b) -> {
            int byCourse = Integer.compare(a.getStudyYear(), b.getStudyYear());
            if (byCourse != 0) return byCourse;
            int byLast = a.getLastName().compareToIgnoreCase(b.getLastName());
            if (byLast != 0) return byLast;
            return a.getFirstName().compareToIgnoreCase(b.getFirstName());
        });
        return result;
    }

    public List<Student> sortedAlphabetically() {
        List<Student> result = new ArrayList<>(storage.values());
        Collections.sort(result, new Comparator<Student>() {
            @Override public int compare(Student a, Student b) {
                int byLast = a.getLastName().compareToIgnoreCase(b.getLastName());
                if (byLast != 0) return byLast;
                return a.getFirstName().compareToIgnoreCase(b.getFirstName());
            }
        });
        return result;
    }
}