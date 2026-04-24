package ua.ukma.edu.service;

import ua.ukma.edu.authorization.AuthorizationService;
import ua.ukma.edu.authorization.Roles;
import ua.ukma.edu.authorization.User;
import ua.ukma.edu.domain.*;
import ua.ukma.edu.dto.StudentDTO;
import ua.ukma.edu.persistence.AutoSaveService;
import ua.ukma.edu.persistence.UniversityStorage;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

public class MainMenu {

    private final Scanner scanner;
    private final University university;
    private final UniversityService universityService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AuthorizationService authorizationService;
    private final User currentUser;
    private final UniversityStorage storage;
    private final AutoSaveService autoSaveService;

    public MainMenu(UniversityService universityService, StudentService studentService, TeacherService teacherService, AuthorizationService authorizationService, User currentUser, UniversityStorage storage, AutoSaveService autoSaveService) {
        this.universityService = universityService;
        this.university = universityService.getUniversity();
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.authorizationService = authorizationService;
        this.currentUser = currentUser;
        this.storage = storage;
        this.autoSaveService = autoSaveService;
        this.scanner = new Scanner(System.in);
    }

    private boolean hasAccess(Roles... role) {
        for(Roles r: role){
            if(currentUser.role().equals(r)){
                return true;
            }
        }
        return false;
    }

    private void deny(){
        System.out.println("Ви не маєте доступу до цього меню.");
    }

    public boolean show() {
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Вітаємо у DigiUni Registry-системі обліку студентів та викладачів НаУКМА");
        System.out.println("------------------------------------------------------------------------");

        boolean running = true;
        while (running) {
            System.out.println("Головне меню");
            System.out.println("1. Робота з університетом");
            System.out.println("2. Робота з факультетом");
            System.out.println("3. Робота з кафедрою");
            System.out.println("4. Робота зі студентом");
            System.out.println("5. Робота з викладачем");
            System.out.println("6. Пошук та звіти");
            System.out.println("7. Користувачі");
            System.out.println("8. Змінити роль");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> universityMenu();
                case "2" -> {
                    if(hasAccess(Roles.ADMIN, Roles.MANAGER))
                        facultyMenu();
                    else deny();}
                case "3" -> {
                    if(hasAccess(Roles.ADMIN, Roles.MANAGER))
                        departmentMenu();
                    else deny();}
                case "4" -> {
                    if(hasAccess(Roles.ADMIN, Roles.MANAGER))
                        studentMenu();
                    else deny();}
                case "5" -> {
                    if(hasAccess(Roles.ADMIN, Roles.MANAGER))
                        teacherMenu();
                    else deny();}
                case "6" -> searchAndReportsMenu();
                case "7" -> {
                    if(hasAccess(Roles.ADMIN))
                        userMenu();
                    else deny();}
                case "8" -> {
                    System.out.println("Змінення ролі...");
                    return true;
                }
                case "0" -> running = false;
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
        System.out.println("Завершено.");
        return running;
    }

    private void userMenu() {
        while (true) {
            System.out.println("Користувачі");
            System.out.println("1. Список");
            System.out.println("2. Додати");
            System.out.println("3. Видалити");
            System.out.println("4. Змінити");
            System.out.println("5. Блокувати");
            System.out.println("6. Розблокувати");
            System.out.println("0. Назад");
            System.out.print("Обрати: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> authorizationService.getAllUsers().forEach(u -> System.out.println(u.username() + " - " + u.role()));
                case "2" -> {
                    System.out.println("Login");
                    String username =  scanner.nextLine();
                    System.out.println("Password");
                    String password =  scanner.nextLine();
                    System.out.println("[0-USER 1-MANAGER 2-ADMIN]");
                    Roles role = Roles.values()[Integer.parseInt(scanner.nextLine())];
                    authorizationService.addUser(username, password, role);
                }
                case "3" -> {
                    System.out.println("Login");
                    authorizationService.removeUser(scanner.nextLine());
                }
                case "4" -> {
                    System.out.println("Login");
                    String username =  scanner.nextLine();
                    System.out.println("[0-USER 1-MANAGER 2-ADMIN]");
                    Roles role = Roles.values()[Integer.parseInt(scanner.nextLine())];
                    authorizationService.changeUser(username, role);
                }
                case "5" -> {
                    System.out.println("Login");
                    authorizationService.blockUser(scanner.nextLine());
                }
                case "6" -> {
                    System.out.println("Login");
                    authorizationService.unblockUser(scanner.nextLine());
                }
                case "0" -> {
                    return;
                }
            }
        }
    }

    private void universityMenu() {
        while (true) {
            System.out.println("Університет: " + university);
            System.out.println("Факультети: ");
            for (Faculty faculty : university.getFaculties()) {
                System.out.println(faculty);
            }
            System.out.println("0. Назад");
            System.out.print("Обрати: ");
            if ("0".equals(scanner.nextLine().trim())) {
                return;
            }
            System.out.println("Невірний вибір. Введіть 0 для повернення.");
        }
    }

    private void facultyMenu() {
        while (true) {
            System.out.println("Меню факультету");
            System.out.println("1. Створити факультет");
            System.out.println("2. Редагувати факультет");
            System.out.println("3. Видалити факультет");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createFaculty();
                case "2" -> editFaculty();
                case "3" -> deleteFaculty();
                case "0" -> { return; }
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
    }

    private void createFaculty() {
        System.out.print("Назва факультету: ");
        String fullName = readNonEmptyLettersOnly();
        System.out.print("Скорочена назва: ");
        String shortName = readNonEmptyLettersOnly();
        System.out.print("Контакти: ");
        String contacts = readNonEmpty();
        Faculty faculty = new Faculty(UUID.randomUUID().toString(), fullName, shortName, contacts, new java.util.ArrayList<>());
        university.getFaculties().add(faculty);
        persist();
        System.out.println("Створено.");
    }

    private void editFaculty() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return;
        System.out.print("Нова назва факультету (поточна: " + faculty.getFullName() + "): ");
        String fullName = readNonEmptyLettersOnly();
        System.out.print("Нова скорочена назва (поточна: " + faculty.getShortName() + "): ");
        String shortName = readNonEmptyLettersOnly();
        System.out.print("Нові контакти (поточні: " + faculty.getContacts() + "): ");
        String contacts = readNonEmpty();
        faculty.setFullName(fullName);
        faculty.setShortName(shortName);
        faculty.setContacts(contacts);
        persist();
        System.out.println("Відредаговано.");
    }

    private void deleteFaculty() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return;

        int departmentsCount = faculty.getDepartments().size();
        int studentsCount = faculty.getDepartments().stream().mapToInt(d -> d.getStudents().size()).sum();
        int teachersCount = faculty.getDepartments().stream().mapToInt(d -> d.getTeachers().size()).sum();

        if (!confirmAction("Видалити факультет '" + faculty.getFullName() + "' разом із " +
                departmentsCount + " кафедрами, " + studentsCount + " студентами та " +
                teachersCount + " викладачами?")) {
            System.out.println("Скасовано.");
            return;
        }

        for (Department dep : faculty.getDepartments()) {
            for (Student student : dep.getStudents()) {
                studentService.deleteStudentById(student.getId());
            }
            for (Teacher teacher : dep.getTeachers()) {
                teacherService.deleteTeacherById(teacher.getId());
            }
        }
        university.getFaculties().remove(faculty);
        persist();
        System.out.println("Видалено.");
    }

    private Faculty selectFaculty() {
        if (university.getFaculties().isEmpty()) {
            System.out.println("Ще немає факультетів.");
            return null;
        }
        for (int i = 0; i < university.getFaculties().size(); i++) {
            System.out.println((i + 1) + ". " + university.getFaculties().get(i).getFullName());
        }
        System.out.println("0. Скасувати");
        System.out.print("Обрати номер факультету: ");
        int num = readInt(0, university.getFaculties().size());
        if (num == 0) return null;
        return university.getFaculties().get(num - 1);
    }

    private void departmentMenu() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return;
        while (true) {
            System.out.println("Меню кафедри (факультет: " + faculty.getFullName() + ")");
            System.out.println("1. Створити кафедру");
            System.out.println("2. Редагувати кафедру");
            System.out.println("3. Видалити кафедру");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createDepartment(faculty);
                case "2" -> editDepartment(faculty);
                case "3" -> deleteDepartment(faculty);
                case "0" -> { return; }
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
    }

    private void createDepartment(Faculty faculty) {
        System.out.print("Назва кафедри: ");
        String name = readNonEmptyLettersOnly();
        System.out.print("Локація: ");
        String location = readNonEmptyLettersOnly();
        Department department = new Department(UUID.randomUUID().toString(), name, location, new Teacher());
        faculty.getDepartments().add(department);
        persist();
        System.out.println("Створено.");
    }

    private void editDepartment(Faculty faculty) {
        Department dep = selectDepartment(faculty);
        if (dep == null) return;
        System.out.print("Нова назва (поточна: " + dep.getName() + "): ");
        String name = readNonEmptyLettersOnly();
        System.out.print("Нова локація (поточна: " + dep.getLocation() + "): ");
        String location = readNonEmptyLettersOnly();
        dep.setName(name);
        dep.setLocation(location);
        persist();
        System.out.println("Відредаговано.");
    }

    private void deleteDepartment(Faculty faculty) {
        Department dep = selectDepartment(faculty);
        if (dep == null) return;

        if (!confirmAction("Видалити кафедру '" + dep.getName() + "' разом із " + dep.getStudents().size() +
                " студентами та " + dep.getTeachers().size() + " викладачами?")) {
            System.out.println("Скасовано.");
            return;
        }

        for (Student student : dep.getStudents()) {
            studentService.deleteStudentById(student.getId());
        }
        for (Teacher teacher : dep.getTeachers()) {
            teacherService.deleteTeacherById(teacher.getId());
        }
        faculty.getDepartments().remove(dep);
        persist();
        System.out.println("Видалено.");
    }

    private Department selectDepartment(Faculty faculty) {
        if (faculty.getDepartments().isEmpty()) {
            System.out.println("Ще немає кафедр.");
            return null;
        }
        for (int i = 0; i < faculty.getDepartments().size(); i++) {
            System.out.println((i + 1) + ". " + faculty.getDepartments().get(i).getName());
        }
        System.out.println("0. Скасувати");
        System.out.print("Обрати номер кафедри: ");
        int num = readInt(0, faculty.getDepartments().size());
        if (num == 0) return null;
        return faculty.getDepartments().get(num - 1);
    }

    private void studentMenu() {
        Department dep = selectAnyDepartment();
        if (dep == null) return;
        while (true) {
            System.out.println("Меню студентів (кафедра: " + dep.getName() + ")");
            System.out.println("1. Додати студента");
            System.out.println("2. Редагувати студента");
            System.out.println("3. Видалити студента");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent(dep);
                case "2" -> editStudent(dep);
                case "3" -> deleteStudent(dep);
                case "0" -> { return; }
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
    }

    private void addStudent(Department dep) {
        System.out.print("Ім'я: "); String firstName = readNonEmptyLettersOnly();
        System.out.print("Прізвище: "); String lastName = readNonEmptyLettersOnly();
        System.out.print("По батькові: "); String patronymic = readNonEmptyLettersOnly();
        System.out.print("Студентський ID: "); String studentId = readNonEmpty();
        System.out.print("Група (1-6): ");
        int groupNumber = readInt(1, 6);
        String group = String.valueOf(groupNumber);
        System.out.print("Курс (1-6): ");
        int studyYear = readInt(1, 6);
        Student student = new Student(UUID.randomUUID().toString(), firstName, lastName, patronymic, java.time.LocalDate.now(), "", "", studentId, group, StudyForm.BUDGET, StudentStatus.ACTIVE, studyYear, 2023);

        dep.getStudents().add(student);
        studentService.saveStudent(student);
        persist();

        System.out.println("Додано.");
    }

    private void editStudent(Department dep) {
        Student student = selectStudent(dep);
        if (student == null) return;
        System.out.print("Нове ім'я (поточне: " + student.getFirstName() + "): ");
        student.setFirstName(readNonEmptyLettersOnly());
        System.out.print("Нове прізвище (поточне: " + student.getLastName() + "): ");
        student.setLastName(readNonEmptyLettersOnly());
        persist();
        System.out.println("Відредаговано.");
    }

    private void deleteStudent(Department dep) {
        Student student = selectStudent(dep);
        if (student == null) return;

        if (!confirmAction("Видалити студента " + student.getFirstName() + " " + student.getLastName() + "?")) {
            System.out.println("Скасовано.");
            return;
        }

        dep.getStudents().remove(student);
        studentService.deleteStudentById(student.getId());
        persist();

        System.out.println("Видалено.");
    }

    private Student selectStudent(Department dep) {
        if (dep.getStudents().isEmpty()) {
            System.out.println("Ще немає студентів.");
            return null;
        }
        for (int i = 0; i < dep.getStudents().size(); i++) {
            Student s = dep.getStudents().get(i);
            System.out.println((i + 1) + ". " + s.getFirstName() + " " + s.getLastName());
        }
        System.out.println("0. Скасувати");
        System.out.print("Обрати номер студента: ");
        int idx = readInt(0, dep.getStudents().size());
        if (idx == 0) return null;
        return dep.getStudents().get(idx - 1);
    }

    private Department selectAnyDepartment() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return null;
        return selectDepartment(faculty);
    }

    private void teacherMenu() {
        Department dep = selectAnyDepartment();
        if (dep == null) return;
        while (true) {
            System.out.println("Меню викладачів (кафедра: " + dep.getName() + ")");
            System.out.println("1. Додати викладача");
            System.out.println("2. Редагувати викладача");
            System.out.println("3. Видалити викладача");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addTeacher(dep);
                case "2" -> editTeacher(dep);
                case "3" -> deleteTeacher(dep);
                case "0" -> { return; }
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
    }

    private void addTeacher(Department dep) {
        System.out.print("Ім'я: ");
        String firstName = readNonEmptyLettersOnly();
        System.out.print("Прізвище: ");
        String lastName = readNonEmptyLettersOnly();
        System.out.print("По батькові: ");
        String patronymic = readNonEmptyLettersOnly();
        System.out.print("Позиція (0-5: ASSISTANT, LECTURER, SENIOR_LECTURER, ASSOCIATE_PROFESSOR, PROFESSOR, HEAD_OF_DEPARTMENT): ");
        Position position = Position.values()[readInt(0, Position.values().length - 1)];
        System.out.print("Науковий ступінь (0-3: BACHELOR, MASTER, PHD, DOCTOR_OF_SCIENCE): ");
        ScientificDegree degree = ScientificDegree.values()[readInt(0, ScientificDegree.values().length - 1)];
        System.out.print("Вчене звання (0-3: NONE, SENIOR_RESEARCHER, DOCENT, PROFESSOR): ");
        AcademicTitle title = AcademicTitle.values()[readInt(0, AcademicTitle.values().length - 1)];
        double rate = 1.0;

        Teacher teacher = new Teacher(UUID.randomUUID().toString(), firstName, lastName, patronymic, LocalDate.now(), "", "", position, degree, title, LocalDate.now(), rate);
        dep.getTeachers().add(teacher);
        teacherService.saveTeacher(teacher);
        persist();
        System.out.println("Додано.");
    }

    private void editTeacher(Department dep) {
        Teacher teacher = selectTeacher(dep);
        if (teacher == null) return;
        System.out.print("Нове ім'я (поточне: " + teacher.getFirstName() + "): ");
        teacher.setFirstName(readNonEmptyLettersOnly());
        System.out.print("Нове прізвище (поточне: " + teacher.getLastName() + "): ");
        teacher.setLastName(readNonEmptyLettersOnly());
        System.out.print("Нове по батькові (поточне: " + teacher.getPatronymic() + "): ");
        teacher.setPatronymic(readNonEmptyLettersOnly());
        teacherService.updateTeacher(teacher);
        persist();
        System.out.println("Відредаговано.");
    }

    private void deleteTeacher(Department dep) {
        Teacher teacher = selectTeacher(dep);
        if (teacher == null) return;

        if (!confirmAction("Видалити викладача " + teacher.getFirstName() + " " + teacher.getLastName() + "?")) {
            System.out.println("Скасовано.");
            return;
        }

        dep.getTeachers().remove(teacher);
        teacherService.deleteTeacherById(teacher.getId());
        persist();
        System.out.println("Видалено: " + teacher.getFirstName() + " " + teacher.getLastName());
    }

    private void persist() {
        autoSaveService.markDirty();
    }

    private Teacher selectTeacher(Department dep) {
        if (dep.getTeachers().isEmpty()) {
            System.out.println("Ще немає викладачів.");
            return null;
        }
        for (int i = 0; i < dep.getTeachers().size(); i++) {
            Teacher t = dep.getTeachers().get(i);
            System.out.println((i + 1) + ". " + t.getFirstName() + " " + t.getLastName());
        }
        System.out.println("0. Скасувати");
        System.out.print("Обрати номер викладача: ");
        int idx = readInt(0, dep.getTeachers().size());
        if (idx == 0) return null;
        return dep.getTeachers().get(idx - 1);
    }

    private void searchAndReportsMenu(){
        while (true) {
            System.out.println("\n Пошук та Звіти ");
            System.out.println("1. Знайти студента (за Прізвищем, Курсом або Групою)");
            System.out.println("2. Звіт: Всі студенти за курсами");
            System.out.println("3. Звіт: Студенти за статусом");
            System.out.println("4. Звіт: Викладачі за посадою");
            System.out.println("5. Звіт: Викладачі за науковим ступенем");
            System.out.println("6. Звіт: Всі факультети");
            System.out.println("7. Звіт: Всі кафедри за факультетами");
            System.out.println("0. Назад");
            System.out.print("Обрати: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> findStudent();
                case "2" -> printAllStudentsSortedByCourse();
                case "3" -> printStudentsByStatus();
                case "4" -> printTeachersByPosition();
                case "5" -> printTeachersByDegree();
                case "6" -> printAllFacultiesReport();
                case "7" -> printDepartmentsGroupedByFacultyReport();
                case "0" -> { return; }
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void findStudent(){
        System.out.println("\nКритерій пошуку:");
        System.out.println("1. Ім'я/Прізвище/По-Батькові");
        System.out.println("2. Курс");
        System.out.println("3. Група");
        System.out.println("0. Назад");
        System.out.print("-> ");
        String type = scanner.nextLine().trim();

        List<StudentDTO> results;

        switch (type){
            case "0" -> { return; }
            case "1" -> {
                System.out.print("Введіть Ім'я/Прізвище/По-Батькові: ");
                results = studentService.findStudentByLnFnMn(readNonEmptyLettersOnly());
            }
            case "2" -> {
                System.out.print("Введіть курс: ");
                results = studentService.findStudentByCourse(readInt(1,6));
            }
            case "3" -> {
                System.out.println("Введіть групу: ");
                results = studentService.findStudentByGroup(readNonEmpty());
            }
            default -> { System.out.println("Невірний вибір."); return; }
        }

        if (results.isEmpty()) {
            System.out.println("Нікого не знайдено.");
        } else {
            System.out.println("\n Знайдено (" + results.size() +"):");
            for (StudentDTO s : results) {
                printFullStudentInfo(s);
            }
        }
    }

    private void printFullStudentInfo(StudentDTO s) {
        System.out.println(" \n===== Особисті дані ======================");
        System.out.println(" - ПІБ: " + s.firstName() + " " + s.lastName() + " " +  s.patronymic());
        System.out.println(" - Дата народження: " + s.birthDate());
        System.out.println(" - Телефон: " + s.phoneNumber());
        System.out.println(" - Email: " + s.email());
        System.out.println(" ===== Навчальні дані =====================");
        System.out.println(" - Залікова: " + s.studentId());
        System.out.println(" - Курс: " + s.studyYear());
        System.out.println(" - Група: " + s.group());
        System.out.println(" - Форма навчання: " + s.studyForm());
        System.out.println(" - Статус: " + s.studentStatus());
        System.out.println(" - Рік вступу: " + s.admissionYear());
        System.out.println(" ==========================================");
        System.out.println(" - ID: " + s.id());
        System.out.println(" ==========================================");
    }

    private void printAllStudentsSortedByCourse(){
        System.out.println("\nЗвіт: Всі студенти за курсами:");
        List<StudentDTO> all = studentService.getStudentsSortedByYear();
        if (all.isEmpty()) {
            System.out.println("Студентів немає.");
        } else {
            int currentYear = 0;
            for (StudentDTO s : all) {
                if (s.studyYear() != currentYear) {
                    currentYear = s.studyYear();
                    System.out.println("\n ======== " + currentYear + " КУРС ==========================");
                }
                printFullStudentInfo(s);
            }
        }
    }

    private void printStudentsByStatus(){
        System.out.println("\nЗвіт: Студенти за статусом");
        var grouped = studentService.getStudentsGroupedByStatus();
        if (grouped.isEmpty()) {
            System.out.println("Студентів немає.");
        } else {
            for (var entry : grouped.entrySet()) {
                System.out.println("\n ======== СТАТУС: " + entry.getKey() + " ==========================");
                for (StudentDTO s : entry.getValue()) {
                    System.out.println("  - " + s.firstName() + " " + s.lastName() + " (курс " + s.studyYear() + ", група " + s.group() + ")");
                }
            }
        }
    }

    private void printTeachersByPosition(){
        System.out.println("\nЗвіт: Викладачі за посадою");
        var grouped = teacherService.getTeachersGroupedByPosition();
        if (grouped.isEmpty()) {
            System.out.println("Викладачів немає.");
        } else {
            for (var entry : grouped.entrySet()) {
                System.out.println("\n ======== ПОСАДА: " + entry.getKey() + " ==========================");
                for (Teacher t : entry.getValue()) {
                    System.out.println("  - " + t.getFirstName() + " " + t.getLastName() + " (стаж: " + t.getExperienceYear() + " років)");
                }
            }
        }
    }

    private void printTeachersByDegree(){
        System.out.println("\nЗвіт: Викладачі за науковим ступенем");
        var grouped = teacherService.getTeachersGroupedByDegree();
        if (grouped.isEmpty()) {
            System.out.println("Викладачів немає.");
        } else {
            for (var entry : grouped.entrySet()) {
                System.out.println("\n ======== СТУПІНЬ: " + entry.getKey() + " ==========================");
                for (Teacher t : entry.getValue()) {
                    System.out.println("  - " + t.getFirstName() + " " + t.getLastName() + " (" + t.getAcademicTitle() + ")");
                }
            }
        }
    }

    private void printAllFacultiesReport() {
        System.out.println("\nЗвіт: Всі факультети");
        List<Faculty> faculties = universityService.getAllFaculties();

        if (faculties.isEmpty()) {
            System.out.println("Факультетів немає.");
            return;
        }

        for (Faculty faculty : faculties) {
            System.out.println("- " + faculty.getFullName() + " (" + faculty.getShortName() + ")");
        }
    }

    private void printDepartmentsGroupedByFacultyReport() {
        System.out.println("\nЗвіт: Всі кафедри за факультетами");
        List<Faculty> faculties = universityService.getAllFaculties();

        if (faculties.isEmpty()) {
            System.out.println("Факультетів немає.");
            return;
        }

        for (Faculty faculty : faculties) {
            System.out.println("\n ======== " + faculty.getFullName() + " (" + faculty.getShortName() + ") ========");
            if (faculty.getDepartments().isEmpty()) {
                System.out.println("  Кафедр немає.");
                continue;
            }

            for (Department dep : faculty.getDepartments()) {
                System.out.println("  - " + dep.getName() + " | " + dep.getLocation());
            }
        }
    }

    private String readNonEmpty() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty())
                return input;
            System.out.print("Не може бути порожнім: ");
        }
    }

    private String readNonEmptyLettersOnly() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("Не може бути порожнім: ");
                continue;
            }
            if(!input.matches("[a-zA-Zа-яА-ЯіІїЇєЄ\\s\\-']+")){
                System.out.println("Вводити тільки літери, пробіли, - та '");
                continue;
            }
            return input;
        }
    }

    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max)
                    return val;
                else System.out.print("Обрати в межах " + min + " - " + max + ": ");
            }
            catch (NumberFormatException e) {
                System.out.print("Невірний вибір. ");
            }
        }
    }

    private boolean confirmAction(String message) {
        while (true) {
            System.out.print(message + " (так/ні): ");
            String answer = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (answer.equals("так") || answer.equals("т") || answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("ні") || answer.equals("н") || answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Введіть 'так' або 'ні'.");
        }
    }
}