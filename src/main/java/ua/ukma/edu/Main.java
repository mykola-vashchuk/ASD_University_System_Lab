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
            MainMenu menu = new MainMenu(universityService, studentService, teacherService, authorizationService, user, autoSaveService);
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

        // Факультет гуманітарних наук (ФГН)
        Faculty humanities = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет гуманітарних наук",
                "ФГН",
                "fgn@ukma.edu.ua",
                new ArrayList<>()
        );

        // Факультет економічних наук (ФЕН)
        Faculty economics = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет економічних наук",
                "ФЕН",
                "fen@ukma.edu.ua",
                new ArrayList<>()
        );

        // Факультет соціальних наук та соціальних технологій (ФСНСТ)
        Faculty socialSciences = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет соціальних наук та соціальних технологій",
                "ФСНСТ",
                "fsnst@ukma.edu.ua",
                new ArrayList<>()
        );

        // Факультет інформатики (ФІ)
        Faculty informatics = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет інформатики",
                "ФІ",
                "fi@ukma.edu.ua",
                new ArrayList<>()
        );

        // Факультет правничих наук (ФПН)
        Faculty law = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет правничих наук",
                "ФПН",
                "fpn@ukma.edu.ua",
                new ArrayList<>()
        );

        // Факультет природничих наук (ФПрН)
        Faculty naturalSciences = new Faculty(
                UUID.randomUUID().toString(),
                "Факультет природничих наук",
                "ФПрН",
                "fprn@ukma.edu.ua",
                new ArrayList<>()
        );

        naukma.getFaculties().add(humanities);
        naukma.getFaculties().add(economics);
        naukma.getFaculties().add(socialSciences);
        naukma.getFaculties().add(informatics);
        naukma.getFaculties().add(law);
        naukma.getFaculties().add(naturalSciences);

        // Деканів факультетів
        // ФГН: Ткачук Марина Леонідівна (професорка, декан)
        Teacher deanHumanities = createTeacher(
                "Марина", "Ткачук", "Леонідівна",
                Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR,
                LocalDate.of(1960, 5, 10), LocalDate.of(1990, 9, 1), 1.0
        );

        // ФЕН: Лук'яненко Ірина Григорівна (професорка, завідувачка кафедри фінансів)
        Teacher deanEconomics = createTeacher(
                "Ірина", "Лук'яненко", "Григорівна",
                Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR,
                LocalDate.of(1958, 3, 15), LocalDate.of(1988, 9, 1), 1.0
        );

        // ФСНСТ: Яковлєва Антоніна Іванівна (доцентка, декан)
        Teacher deanSocialSciences = createTeacher(
                "Антоніна", "Яковлєва", "Іванівна",
                Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT,
                LocalDate.of(1962, 8, 22), LocalDate.of(1992, 9, 1), 1.0
        );

        // ФІ: Глибовець Андрій Миколайович (декан факультету)
        Teacher deanInformatics = createTeacher(
                "Андрій", "Глибовець", "Миколайович",
                Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR,
                LocalDate.of(1955, 11, 3), LocalDate.of(1985, 9, 1), 1.0
        );

        // ФПН: Венгер Володимир Миколайович (доцент, декан)
        Teacher deanLaw = createTeacher(
                "Володимир", "Венгер", "Миколайович",
                Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT,
                LocalDate.of(1963, 6, 18), LocalDate.of(1993, 9, 1), 1.0
        );

        // ФПрН: Білоус Олександр Анатолійович (доцент, декан)
        Teacher deanNaturalSciences = createTeacher(
                "Олександр", "Білоус", "Анатолійович",
                Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT,
                LocalDate.of(1961, 9, 7), LocalDate.of(1991, 9, 1), 1.0
        );

        humanities.setDean(deanHumanities);
        economics.setDean(deanEconomics);
        socialSciences.setDean(deanSocialSciences);
        informatics.setDean(deanInformatics);
        law.setDean(deanLaw);
        naturalSciences.setDean(deanNaturalSciences);

        // Кафедри та викладачі для Факультету гуманітарних наук (ФГН)
        // Семків Ростислав Андрійович (доцент, кафедра літературознавства)
        Department literature = new Department(
                UUID.randomUUID().toString(),
                "Кафедра літературознавства",
                "Корпус 3, поверх 2",
                createTeacher("Ростислав", "Семків", "Андрійович", Position.HEAD_OF_DEPARTMENT, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1964, 4, 12), LocalDate.of(1994, 9, 1), 1.0)
        );
        // Агеєва Віра Павлівна (професорка, кафедра літературознавства)
        literature.getTeachers().add(createTeacher("Віра", "Агеєва", "Павлівна", Position.PROFESSOR, ScientificDegree.PHD, AcademicTitle.PROFESSOR, LocalDate.of(1952, 7, 25), LocalDate.of(1982, 9, 1), 1.0));
        humanities.getDepartments().add(literature);

        // Кафедри та викладачі для Факультету економічних наук (ФЕН)
        // Гуменна Олександра Віталіївна (доцентка, кафедра маркетингу та управління бізнесом)
        Department marketing = new Department(
                UUID.randomUUID().toString(),
                "Кафедра маркетингу та управління бізнесом",
                "Корпус 4, поверх 1",
                createTeacher("Олександра", "Гуменна", "Віталіївна", Position.HEAD_OF_DEPARTMENT, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1965, 2, 19), LocalDate.of(1995, 9, 1), 1.0)
        );
        economics.getDepartments().add(marketing);

        Department finance = new Department(
                UUID.randomUUID().toString(),
                "Кафедра фінансів",
                "Корпус 4, поверх 2",
                deanEconomics
        );
        economics.getDepartments().add(finance);

        // Кафедри та викладачі для Факультету соціальних наук та соціальних технологій (ФСНСТ)
        // Оксамитна Світлана Миколаївна (професорка, кафедра соціології)
        Department sociology = new Department(
                UUID.randomUUID().toString(),
                "Кафедра соціології",
                "Корпус 5, поверх 1",
                createTeacher("Світлана", "Оксамитна", "Миколаївна", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1956, 1, 14), LocalDate.of(1986, 9, 1), 1.0)
        );
        // Паніотто Володимир Ілліч (професор, кафедра соціології)
        sociology.getTeachers().add(createTeacher("Володимир", "Паніотто", "Ілліч", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1954, 10, 8), LocalDate.of(1984, 9, 1), 1.0));
        socialSciences.getDepartments().add(sociology);

        // Кафедри та викладачі для Факультету інформатики (ФІ) - великий список
        // Глибовець Микола Миколайович (професор, завідувач кафедри комп'ютерних наук)
        Department computerScienceDept = new Department(
                UUID.randomUUID().toString(),
                "Кафедра комп'ютерних наук",
                "Корпус 1, поверх 4",
                createTeacher("Микола", "Глибовець", "Миколайович", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1953, 12, 25), LocalDate.of(1983, 9, 1), 1.0)
        );

        // Богданський Юрій Вікторович (професор кафедри математики)
        Department mathDept = new Department(
                UUID.randomUUID().toString(),
                "Кафедра математики",
                "Корпус 1, поверх 5",
                createTeacher("Юрій", "Богданський", "Вікторович", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1950, 6, 16), LocalDate.of(1980, 9, 1), 1.0)
        );
        // Трохимчук Володимир Віталійович (доцент кафедри математики)
        mathDept.getTeachers().add(createTeacher("Володимир", "Трохимчук", "Віталійович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1959, 3, 9), LocalDate.of(1989, 9, 1), 1.0));
        // Іванок Ігор Володимирович (старший викладач кафедри математики)
        mathDept.getTeachers().add(createTeacher("Ігор", "Іванок", "Володимирович", Position.SENIOR_LECTURER, ScientificDegree.MASTER, AcademicTitle.SENIOR_RESEARCHER, LocalDate.of(1970, 8, 20), LocalDate.of(2000, 9, 1), 1.0));

        // Бублик Володимир Васильович (доцент, завідувач кафедри мультимедійних систем)
        Department multimediaDept = new Department(
                UUID.randomUUID().toString(),
                "Кафедра мультимедійних систем",
                "Корпус 1, поверх 3",
                createTeacher("Володимир", "Бублик", "Васильович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1960, 9, 11), LocalDate.of(1990, 9, 1), 1.0)
        );

        // Бойко Юрій Володимирович (доцент, завідувач кафедри мережних технологій)
        Department networkDept = new Department(
                UUID.randomUUID().toString(),
                "Кафедра мережних технологій",
                "Корпус 1, поверх 6",
                createTeacher("Юрій", "Бойко", "Володимирович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1958, 11, 27), LocalDate.of(1988, 9, 1), 1.0)
        );

        // Лозицький Олександр Андрійович (доцент кафедри інформаційних систем і технологій)
        Department informationSystemsDept = new Department(
                UUID.randomUUID().toString(),
                "Кафедра інформаційних систем і технологій",
                "Корпус 2, поверх 1",
                createTeacher("Олександр", "Лозицький", "Андрійович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1961, 5, 30), LocalDate.of(1991, 9, 1), 1.0)
        );

        // Карнаух Анатолій Олександрович (доцент кафедри комп'ютерних наук)
        computerScienceDept.getTeachers().add(createTeacher("Анатолій", "Карнаух", "Олександрович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1957, 2, 14), LocalDate.of(1987, 9, 1), 1.0));
        // Ісаєв Дмитро Сергійович (доцент кафедри комп'ютерних наук)
        computerScienceDept.getTeachers().add(createTeacher("Дмитро", "Ісаєв", "Сергійович", Position.ASSOCIATE_PROFESSOR, ScientificDegree.PHD, AcademicTitle.DOCENT, LocalDate.of(1962, 10, 5), LocalDate.of(1992, 9, 1), 1.0));

        informatics.getDepartments().add(computerScienceDept);
        informatics.getDepartments().add(mathDept);
        informatics.getDepartments().add(multimediaDept);
        informatics.getDepartments().add(networkDept);
        informatics.getDepartments().add(informationSystemsDept);

        // Кафедри та викладачі для Факультету правничих наук (ФПН)
        // Азаров Денис Сергійович (професор, кафедра кримінального та кримінального процесуального права)
        Department criminalLaw = new Department(
                UUID.randomUUID().toString(),
                "Кафедра кримінального та кримінального процесуального права",
                "Корпус 6, поверх 2",
                createTeacher("Денис", "Азаров", "Сергійович", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1954, 4, 21), LocalDate.of(1984, 9, 1), 1.0)
        );
        law.getDepartments().add(criminalLaw);

        // Кафедри та викладачі для Факультету природничих наук (ФПрН)
        // Білько Ніна Михайлівна (професорка, кафедра лабораторної діагностики біологічних систем)
        Department biology = new Department(
                UUID.randomUUID().toString(),
                "Кафедра лабораторної діагностики біологічних систем",
                "Корпус 7, поверх 1",
                createTeacher("Ніна", "Білько", "Михайлівна", Position.PROFESSOR, ScientificDegree.DOCTOR_OF_SCIENCE, AcademicTitle.PROFESSOR, LocalDate.of(1951, 9, 12), LocalDate.of(1981, 9, 1), 1.0)
        );
        naturalSciences.getDepartments().add(biology);

        // Додавання студентів до кафедр
        computerScienceDept.getStudents().add(new Student(UUID.randomUUID().toString(),
                "Benedict", "Bridgerton", "Mikolkovich",
                LocalDate.of(2004, 5, 20), "bb.sofiaOneLove@ukma.edu.ua", "+380656056060",
                "КВ-123456", "2", StudyForm.BUDGET, StudentStatus.ACTIVE, 3, 2023
        ));
        computerScienceDept.getStudents().add(new Student(UUID.randomUUID().toString(),
                "Мирон", "Хняч", "Йосипович",
                LocalDate.of(2006, 9, 25), "myron.hnyach@ukma.edu.ua", "+380555555555",
                "КВ-654321", "4", StudyForm.CONTRACT, StudentStatus.ACTIVE, 2, 2024
        ));
        mathDept.getStudents().add(new Student(UUID.randomUUID().toString(),
                "Dafni", "Bridgerton", "Володимирівна",
                LocalDate.of(2008, 2, 12), "dafni.bridgerton.symonOneLove@ukma.edu.ua", "+380777777777",
                "КВ-654575", "1", StudyForm.BUDGET, StudentStatus.ACTIVE, 1, 2025
        ));

        return naukma;
    }

    private static Teacher createTeacher(String firstName, String lastName, String patronymic,
                                         Position position, ScientificDegree degree, AcademicTitle title,
                                         LocalDate birthDate, LocalDate hireDate, double rate) {
        return new Teacher(
                UUID.randomUUID().toString(),
                firstName,
                lastName,
                patronymic,
                birthDate,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@ukma.edu.ua",
                "+380500000000",
                position,
                degree,
                title,
                hireDate,
                rate
        );
    }
}