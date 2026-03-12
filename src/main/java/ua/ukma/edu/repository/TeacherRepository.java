package ua.ukma.edu.repository;

import ua.ukma.edu.domain.Teacher;
import java.util.*;

public class TeacherRepository extends InMemoryRepository<Teacher, String> {

    @Override
    protected String getId(Teacher entity) { return entity.getId(); }

    public List<Teacher> findByLastName(String lastName) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher t : storage.values()) if (t.getLastName().equalsIgnoreCase(lastName)) result.add(t);
        return result;
    }

    public List<Teacher> sortedAlphabetically() {
        List<Teacher> result = new ArrayList<>(storage.values());
        Collections.sort(result, new Comparator<Teacher>() {
            @Override public int compare(Teacher a, Teacher b) {
                int byLast = a.getLastName().compareToIgnoreCase(b.getLastName());
                if (byLast != 0) return byLast;
                return a.getFirstName().compareToIgnoreCase(b.getFirstName());
            }
        });
        return result;
    }
}
