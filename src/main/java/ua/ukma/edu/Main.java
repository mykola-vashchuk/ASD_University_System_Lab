package ua.ukma.edu;

import ua.ukma.edu.authorization.AuthorizationService;
import ua.ukma.edu.authorization.User;
import ua.ukma.edu.domain.*;
import ua.ukma.edu.persistence.AutoSaveService;
import ua.ukma.edu.persistence.UniversityStorage;
import ua.ukma.edu.repository.Repository;
import ua.ukma.edu.repository.StudentRepository;
import ua.ukma.edu.repository.TeacherRepository;
import ua.ukma.edu.service.MainMenu;
import ua.ukma.edu.service.StudentService;
import ua.ukma.edu.service.TeacherService;
import ua.ukma.edu.service.UniversityService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UniversityStorage storage = new UniversityStorage();
        University naukma = storage.loadOrDefault(Main::createDefaultUniversity);
        System.out.println("Завантажено університет: " + naukma.getShortName());

        // ініціалізація репозиторію зі студентами з поточного стану університету
        Repository<Student, String> studentRepository = new StudentRepository();
        for (Faculty faculty : naukma.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                for (Student student : department.getStudents()) {
                    studentRepository.save(student);
                }
            }
        }

        // ініціалізація репозиторію з викладачами з поточного стану університету
        Repository<Teacher, String> teacherRepository = new TeacherRepository();
        for (Faculty faculty : naukma.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                for (Teacher teacher : department.getTeachers()) {
                    teacherRepository.save(teacher);
                }
            }
        }

        // ініціалізація сервісів
        UniversityService universityService = new UniversityService(naukma);
        StudentService studentService = new StudentService(studentRepository);
        TeacherService teacherService = new TeacherService(teacherRepository);
        AuthorizationService authorizationService = new AuthorizationService();
        AutoSaveService autoSaveService = new AutoSaveService(storage, universityService::getUniversity, 30);
        autoSaveService.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            User user = login(scanner, authorizationService);

            if(user == null) {
                autoSaveService.stop();
                System.out.println("Завершення роботи.");
                break;
            }
            System.out.println("Авторизація успішна: " + user.username() + " " + user.role());
            MainMenu menu = new MainMenu(universityService, studentService, teacherService, authorizationService, user, storage, autoSaveService);
            boolean logout = menu.show();
            if (!logout) {
                autoSaveService.stop();
                break;
            }
        }
    }

    private static User login(Scanner scanner, AuthorizationService authorizationService) {
        User user = null;

        int attempts = 3;
        System.out.println("Кількість спроб: "+attempts);
        while(user == null && attempts > 0){
            System.out.print("Login: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            Optional<User> optionalUser = authorizationService.login(username, password);

            if(optionalUser.isPresent()){
                user = optionalUser.get();
            }
            else{
                attempts--;
                System.out.println("Невірний login або password. Залишилось спроб: " + attempts);
            }
        }

        if(user == null){
            System.out.println("Кількість спроб вичерпана.");
        }
            return user;
    }

    private static University createDefaultUniversity() {
        University naukma = new University(
                "Національний університет Києво-Могилянська академія",
                "НаУКМА",
                "Київ",
                "Григорія Сковороди 2"
        );

        Faculty fi = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет Інформатики",
                "ФІ",
                "fi@ukma.edu.ua",
                new ArrayList<>()
        );
        naukma.getFaculties().add(fi);

        Department ipz = new Department(
                UUID.randomUUID().toString(),
                "Інженерія ПЗ",
                "Корпус 1, поверх 3",
                new Teacher()
        );
        fi.getDepartments().add(ipz);

        Student student = new Student(UUID.randomUUID().toString(),
                "Benedict", "Bridgerton", "Mikolkovich",
                LocalDate.of(2004, 5, 20), "bb.sofiaOneLove@ukma.edu.ua", "+380656056060",
                "КВ-123456", "2", StudyForm.BUDGET, StudentStatus.ACTIVE, 3, 2023
        );
        ipz.getStudents().add(student);

        Student student2 = new Student(UUID.randomUUID().toString(),
                "Мирон", "Хняч", "Йосипович",
                LocalDate.of(2006, 9, 25), "myron.hnyach@ukma.edu.ua", "+380555555555",
                "КВ-654321", "4", StudyForm.CONTRACT, StudentStatus.ACTIVE, 2, 2024
        );
        ipz.getStudents().add(student2);

        Student student3 = new Student(UUID.randomUUID().toString(),
                "Dafni", "Bridgerton", "Володимирівна",
                LocalDate.of(2008, 2, 12), "dafni.bridgerton.symonOneLove@ukma.edu.ua", "+380777777777",
                "КВ-654575", "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 1, 2025
        );
        ipz.getStudents().add(student3);

        return naukma;
    }
}