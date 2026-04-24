package ua.ukma.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ukma.edu.domain.Student;
import ua.ukma.edu.domain.StudentStatus;
import ua.ukma.edu.domain.StudyForm;
import ua.ukma.edu.exception.DuplicateEntityException;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.StudentRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRepositoryTests {
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        studentRepository = new StudentRepository();
    }

    private Student createStudent(String id) {
        return new Student(
                id,
                "Іван", "Петренко", "Тестович",
                LocalDate.of(2000, 1, 1),
                "test@example.com", "+380123456789",
                UUID.randomUUID().toString(),
                "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 1, 2023
        );
    }

    @Test
    public void testSaveStudent() {
        Student student = createStudent(UUID.randomUUID().toString());
        studentRepository.save(student);

        assertTrue(studentRepository.findById(student.getId()).isPresent());
    }

    @Test
    public void testSaveDuplicateThrowsException() {
        String id = UUID.randomUUID().toString();
        Student student1 = createStudent(id);
        studentRepository.save(student1);

        Student student2 = createStudent(id);
        assertThrows(DuplicateEntityException.class, () -> studentRepository.save(student2));
    }

    @Test
    public void testFindById() {
        String id = UUID.randomUUID().toString();
        Student student = createStudent(id);
        studentRepository.save(student);

        assertTrue(studentRepository.findById(id).isPresent());
    }

    @Test
    public void testFindNonExistent() {
        assertTrue(studentRepository.findById("nonexistent").isEmpty());
    }

    @Test
    public void testDeleteById() {
        String id = UUID.randomUUID().toString();
        Student student = createStudent(id);
        studentRepository.save(student);

        studentRepository.deleteById(id);
        assertTrue(studentRepository.findById(id).isEmpty());
    }

    @Test
    public void testDeleteNonExistentThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> studentRepository.deleteById("nonexistent"));
    }

    @Test
    public void testFindAll() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        studentRepository.save(createStudent(id1));
        studentRepository.save(createStudent(id2));

        assertEquals(2, studentRepository.findAll().size());
    }
}

