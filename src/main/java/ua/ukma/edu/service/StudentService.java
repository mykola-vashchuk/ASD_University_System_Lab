package ua.ukma.edu.service;

import ua.ukma.edu.domain.Student;
import ua.ukma.edu.dto.StudentDTO;
import ua.ukma.edu.dto.StudentMapper;
import ua.ukma.edu.exception.EntityNotFoundException;
import ua.ukma.edu.repository.Repository;

import java.util.*;

public class StudentService {
    private final Repository<Student, String> studentRepository;

    public StudentService(Repository<Student, String> studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findOptionalById(String id) {
        return studentRepository.findById(id);
    }

    public Student findById(String id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID: " + id + " не знайдено."));
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(String id) {
        studentRepository.deleteById(id);
    }

    public List<StudentDTO> findStudentByLnFnMn(String value){
        return studentRepository.findAll().stream()
                .filter(student ->
                        student.getFirstName().equalsIgnoreCase(value)||
                        student.getLastName().equalsIgnoreCase(value)||
                        student.getPatronymic().equalsIgnoreCase(value)
                )
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> getStudentsSortedByYear(){
        return studentRepository.findAll().stream()
                .sorted((s1, s2) -> Integer.compare(s1.getStudyYear(), s2.getStudyYear()))
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> findStudentByCourse(int course){
        return studentRepository.findAll().stream()
                .filter(student -> student.getStudyYear() == course)
                .map(StudentMapper::toDTO)
                .toList();
    }
    public List<StudentDTO> findStudentByGroup(String group){
        return studentRepository.findAll().stream()
                .filter(student -> student.getGroup().equalsIgnoreCase(group))
                .map(StudentMapper::toDTO)
                .toList();
    }
}