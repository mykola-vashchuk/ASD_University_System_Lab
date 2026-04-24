package ua.ukma.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.ukma.edu.domain.Student;
import ua.ukma.edu.domain.StudentStatus;
import ua.ukma.edu.domain.StudyForm;
import ua.ukma.edu.dto.StudentDTO;
import ua.ukma.edu.repository.StudentRepository;
import ua.ukma.edu.service.StudentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTests {
    private StudentService studentService;
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        studentRepository = new StudentRepository();
        studentService = new StudentService(studentRepository);
    }

    private Student createStudent(String firstName, String lastName, int studyYear, String group, StudentStatus status) {
        return new Student(
                UUID.randomUUID().toString(),
                firstName, lastName, "Тестович",
                LocalDate.of(2000, 1, 1),
                "test@example.com", "+380123456789",
                UUID.randomUUID().toString(),
                group, StudyForm.BUDGET, status, studyYear, 2023
        );
    }

    @Test
    public void testSaveAndFindStudent() {
        Student student = createStudent("Іван", "Петренко", 2, "1", StudentStatus.ACTIVE);
        studentService.saveStudent(student);

        assertTrue(studentService.findOptionalById(student.getId()).isPresent());
    }

    @Test
    public void testFindStudentByName() {
        Student student1 = createStudent("Іван", "Петренко", 2, "1", StudentStatus.ACTIVE);
        Student student2 = createStudent("Марія", "Коваленко", 3, "2", StudentStatus.ACTIVE);
        studentService.saveStudent(student1);
        studentService.saveStudent(student2);

        List<StudentDTO> results = studentService.findStudentByLnFnMn("Іван");
        assertEquals(1, results.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testFindStudentByCourse(int course) {
        Student student = createStudent("Іван", "Петренко", course, "1", StudentStatus.ACTIVE);
        studentService.saveStudent(student);

        List<StudentDTO> results = studentService.findStudentByCourse(course);
        assertEquals(1, results.size());
        assertEquals(course, results.get(0).studyYear());
    }

    @Test
    public void testFindStudentByGroup() {
        Student student = createStudent("Іван", "Петренко", 2, "3", StudentStatus.ACTIVE);
        studentService.saveStudent(student);

        List<StudentDTO> results = studentService.findStudentByGroup("3");
        assertEquals(1, results.size());
    }

    @Test
    public void testGetStudentsSortedByYear() {
        Student year1 = createStudent("Аліса", "Петренко", 1, "1", StudentStatus.ACTIVE);
        Student year2 = createStudent("Боб", "Коваленко", 2, "2", StudentStatus.ACTIVE);
        Student year3 = createStudent("Вероніка", "Іванова", 3, "3", StudentStatus.ACTIVE);
        
        studentService.saveStudent(year2);
        studentService.saveStudent(year1);
        studentService.saveStudent(year3);

        List<StudentDTO> sorted = studentService.getStudentsSortedByYear();
        assertEquals(3, sorted.size());
        assertEquals(1, sorted.get(0).studyYear());
        assertEquals(2, sorted.get(1).studyYear());
        assertEquals(3, sorted.get(2).studyYear());
    }

    @Test
    public void testGetStudentsGroupedByStatus() {
        Student active = createStudent("Іван", "Петренко", 2, "1", StudentStatus.ACTIVE);
        Student academic = createStudent("Марія", "Коваленко", 2, "1", StudentStatus.ACADEMIC_LEAVE);
        Student expelled = createStudent("Петро", "Іванов", 2, "1", StudentStatus.EXPELLED);
        
        studentService.saveStudent(active);
        studentService.saveStudent(academic);
        studentService.saveStudent(expelled);

        Map<StudentStatus, List<StudentDTO>> grouped = studentService.getStudentsGroupedByStatus();

        assertTrue(grouped.containsKey(StudentStatus.ACTIVE));
        assertTrue(grouped.containsKey(StudentStatus.ACADEMIC_LEAVE));
        assertTrue(grouped.containsKey(StudentStatus.EXPELLED));
        assertEquals(1, grouped.get(StudentStatus.ACTIVE).size());
        assertEquals(1, grouped.get(StudentStatus.ACADEMIC_LEAVE).size());
        assertEquals(1, grouped.get(StudentStatus.EXPELLED).size());
    }

    @Test
    public void testGetStudentsGroupedByYear() {
        Student year1 = createStudent("Іван", "Петренко", 1, "1", StudentStatus.ACTIVE);
        Student year2 = createStudent("Марія", "Коваленко", 2, "2", StudentStatus.ACTIVE);
        Student year1b = createStudent("Петро", "Іванов", 1, "1", StudentStatus.ACTIVE);
        
        studentService.saveStudent(year1);
        studentService.saveStudent(year2);
        studentService.saveStudent(year1b);

        Map<Integer, List<StudentDTO>> grouped = studentService.getStudentsGroupedByYear();

        assertEquals(2, grouped.size());
        assertTrue(grouped.containsKey(1));
        assertTrue(grouped.containsKey(2));
        assertEquals(2, grouped.get(1).size());
        assertEquals(1, grouped.get(2).size());
    }

    @Test
    public void testDeleteStudent() {
        Student student = createStudent("Іван", "Петренко", 2, "1", StudentStatus.ACTIVE);
        studentService.saveStudent(student);
        
        studentService.deleteStudentById(student.getId());
        assertTrue(studentService.getAllStudents().isEmpty());
    }

    @Test
    public void testValidateStudentThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> studentService.saveStudent(null));
    }

    @Test
    public void testValidateStudentThrowsOnEmptyName() {
        Student student = new Student(
                UUID.randomUUID().toString(),
                "", "Петренко", "Тестович",
                LocalDate.of(2000, 1, 1),
                "test@example.com", "+380123456789",
                UUID.randomUUID().toString(),
                "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 1, 2023
        );
        
        assertThrows(IllegalArgumentException.class, () -> studentService.saveStudent(student));
    }

    @Test
    public void testValidateStudentThrowsOnInvalidYear() {
        Student student = new Student(
                UUID.randomUUID().toString(),
                "Іван", "Петренко", "Тестович",
                LocalDate.of(2000, 1, 1),
                "test@example.com", "+380123456789",
                UUID.randomUUID().toString(),
                "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 10, 2023
        );
        
        assertThrows(IllegalArgumentException.class, () -> studentService.saveStudent(student));
    }

    @Test
    public void testFindStudentByNameNotFound() {
        List<StudentDTO> results = studentService.findStudentByLnFnMn("Неіснуюче імя");
        assertTrue(results.isEmpty());
    }
}

