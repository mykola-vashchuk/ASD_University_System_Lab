package ua.ukma.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ukma.edu.domain.AcademicTitle;
import ua.ukma.edu.domain.Position;
import ua.ukma.edu.domain.ScientificDegree;
import ua.ukma.edu.domain.Teacher;
import ua.ukma.edu.repository.TeacherRepository;
import ua.ukma.edu.service.TeacherService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherServiceTests {
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        teacherService = new TeacherService(new TeacherRepository());
    }

    private Teacher createTeacher(String firstName, String lastName, Position position, ScientificDegree degree) {
        return new Teacher(
                UUID.randomUUID().toString(),
                firstName, lastName, "Петрович",
                LocalDate.of(1980, 1, 1),
                "teacher@example.com", "+380123456789",
                position, degree, AcademicTitle.PROFESSOR,
                LocalDate.of(2020, 1, 1), 1.0
        );
    }

    @Test
    public void testSaveAndFindTeacher() {
        Teacher teacher = createTeacher("Іван", "Петренко", Position.PROFESSOR, ScientificDegree.PHD);
        teacherService.saveTeacher(teacher);

        assertTrue(teacherService.findOptionalById(teacher.getId()).isPresent());
    }

    @Test
    public void testFindTeacherByName() {
        Teacher teacher1 = createTeacher("Іван", "Петренко", Position.PROFESSOR, ScientificDegree.PHD);
        Teacher teacher2 = createTeacher("Марія", "Коваленко", Position.LECTURER, ScientificDegree.MASTER);
        teacherService.saveTeacher(teacher1);
        teacherService.saveTeacher(teacher2);

        List<Teacher> results = teacherService.findTeacherByLnFnMn("Іван");
        assertEquals(1, results.size());
    }

    @Test
    public void testGetTeachersGroupedByPosition() {
        Teacher prof = createTeacher("Іван", "Петренко", Position.PROFESSOR, ScientificDegree.PHD);
        Teacher lecturer = createTeacher("Марія", "Коваленко", Position.LECTURER, ScientificDegree.MASTER);
        teacherService.saveTeacher(prof);
        teacherService.saveTeacher(lecturer);

        Map<Position, List<Teacher>> grouped = teacherService.getTeachersGroupedByPosition();

        assertTrue(grouped.containsKey(Position.PROFESSOR));
        assertTrue(grouped.containsKey(Position.LECTURER));
    }

    @Test
    public void testGetTeachersGroupedByDegree() {
        Teacher phd = createTeacher("Іван", "Петренко", Position.PROFESSOR, ScientificDegree.PHD);
        Teacher master = createTeacher("Марія", "Коваленко", Position.LECTURER, ScientificDegree.MASTER);
        teacherService.saveTeacher(phd);
        teacherService.saveTeacher(master);

        Map<ScientificDegree, List<Teacher>> grouped = teacherService.getTeachersGroupedByDegree();

        assertTrue(grouped.containsKey(ScientificDegree.PHD));
        assertTrue(grouped.containsKey(ScientificDegree.MASTER));
    }

    @Test
    public void testDeleteTeacher() {
        Teacher teacher = createTeacher("Іван", "Петренко", Position.PROFESSOR, ScientificDegree.PHD);
        teacherService.saveTeacher(teacher);

        teacherService.deleteTeacherById(teacher.getId());
        assertTrue(teacherService.getAllTeachers().isEmpty());
    }
}

