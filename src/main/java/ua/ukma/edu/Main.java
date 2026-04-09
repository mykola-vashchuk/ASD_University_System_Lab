package ua.ukma.edu;

import ua.ukma.edu.authorization.AuthorizationService;
import ua.ukma.edu.authorization.User;
import ua.ukma.edu.domain.*;
import ua.ukma.edu.repository.Repository;
import ua.ukma.edu.repository.StudentRepository;
import ua.ukma.edu.service.MainMenu;
import ua.ukma.edu.service.StudentService;
import ua.ukma.edu.service.UniversityService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        University naukma = new University(
                "Національний університет Києво-Могилянська академія",
                "НаУКМА",
                "Київ",
                "Григорія Сковороди 2"
        );
        System.out.println("Створено університет: " + naukma.getShortName());

        Faculty fi = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет Інформатики",
                "ФІ",
                "fi@ukma.edu.ua",
                new ArrayList<>()
        );
        naukma.getFaculties().add(fi);
        System.out.println("Додано факультет: " + fi.getShortName());

        Department ipz = new Department(
                UUID.randomUUID().toString(),
                "Інженерія ПЗ",
                "Корпус 1, поверх 3",
                new Teacher()
        );
        fi.getDepartments().add(ipz);
        System.out.println("Додано кафедру(спеціальність): " + ipz.getName());

        Student student = new Student(UUID.randomUUID().toString(),
                "Benedict", "Bridgerton", "Mikolkovich",
                LocalDate.of(2004, 5, 20), "bb.sofiaOneLove@ukma.edu.ua", "+380656056060",
                "КВ-123456", "2", StudyForm.BUDGET, StudentStatus.ACTIVE, 3, 2023
        );
        ipz.getStudents().add(student);
        System.out.println("Додано студента: " + student.getLastName());

        Student student2 = new Student(UUID.randomUUID().toString(),
                "Мирон", "Хняч", "Йосипович",
                LocalDate.of(2006, 9, 25), "myron.hnyach@ukma.edu.ua", "+380555555555",
                "КВ-654321", "4", StudyForm.CONTRACT, StudentStatus.ACTIVE, 2, 2024
        );
        ipz.getStudents().add(student2);
        System.out.println("Додано студента: " + student2.getLastName());

        Student student3 = new Student(UUID.randomUUID().toString(),
                "Dafni", "Bridgerton", "Володимирівна",
                LocalDate.of(2008, 2, 12), "dafni.bridgerton.symonOneLove@ukma.edu.ua", "+380777777777",
                "КВ-654575", "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 1, 2025
        );
        ipz.getStudents().add(student3);
        System.out.println("Додано студента: " + student3.getLastName());

        // ініціалізація репозиторію та збереження студентів
        Repository<Student, String> studentRepository = new StudentRepository();
        studentRepository.save(student);
        studentRepository.save(student2);
        studentRepository.save(student3);

        // ініціалізація сервісів
        UniversityService universityService = new UniversityService(naukma);
        StudentService studentService = new StudentService(studentRepository);
        AuthorizationService authorizationService = new AuthorizationService();
        Scanner scanner = new Scanner(System.in);

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
            return;
        }

        System.out.println("Авторизація успішна: " + user.getUsername() + " " + user.getRole());

        // передача сервісів у меню
        MainMenu menu = new MainMenu(universityService, studentService, authorizationService, user);
        menu.show();
    }
}