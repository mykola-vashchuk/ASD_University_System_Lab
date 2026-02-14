
package ua.ukma.edu.service;

import ua.ukma.edu.domain.*;
import java.util.UUID;

import java.util.*;

public class MainMenu {


    private static final Scanner scanner = new Scanner(System.in);


    private static final University university = new University(

            "Національний університет Києво-Могилянська академія",
            "НаУКМА",
            "Київ",
            "Григорія Сковороди 2"
    );

    //main menu
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Вітаємо у DigiUni Registry-системі обліку студентів та викладачів НаУКМА");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Головне меню");
            System.out.println("1. Робота з університетом");
            System.out.println("2. Робота з факультетом");
            System.out.println("3. Робота з кафедрою");
            System.out.println("4. Робота зі студентом");
            System.out.println("5. Робота з викладачем");
            System.out.println("0. Вихід");
            System.out.print("Обрати: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> universityMenu();
                case "2" -> facultyMenu();
                case "3" -> departmentMenu();
                case "4" -> studentMenu();
                case "5" -> teacherMenu();
                case "0" -> running = false;
                default -> System.out.println("Невірний вибір. Введіть коректне значення.");
            }
        }
        System.out.println("Завершено.");
    }

    //university
    private static void universityMenu() {
        System.out.println("Університет: " + university);
        System.out.println("Факультети: ");
        for (Faculty faculty : university.getFaculties()) {
            System.out.println(faculty);
        }
    }

    //faculty
    private static void facultyMenu() {
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

    private static void createFaculty() {
        System.out.print("Назва факультету: ");
        String fullName = readNonEmpty();
        System.out.print("Скорочена назва: ");
        String shortName = readNonEmpty();
        System.out.print("Контакти: ");
        String contacts = readNonEmpty();
        Faculty faculty = new Faculty(UUID.randomUUID().toString(), fullName, shortName, contacts, null);
        university.getFaculties().add(faculty);
        System.out.println("Створено.");
    }

    private static void editFaculty() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return;
        System.out.print("Нова назва факультету (поточна: " + faculty.getFullName() + "): ");
        String fullName = readNonEmpty();
        System.out.print("Нова скорочена назва (поточна: " + faculty.getShortName() + "): ");
        String shortName = readNonEmpty();
        System.out.print("Нові контакти (поточні: " + faculty.getContacts() + "): ");
        String contacts = readNonEmpty();
        faculty.setFullName(fullName);
        faculty.setShortName(shortName);
        faculty.setContacts(contacts);
        System.out.println("Відредаговано.");
    }

    private static void deleteFaculty() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return;
        university.getFaculties().remove(faculty);
        System.out.println("Видалено.");
    }

    private static Faculty selectFaculty() {
        if (university.getFaculties().isEmpty()) {
            System.out.println("Ще немає факультетів.");
            return null;
        }
        for (int i = 0; i < university.getFaculties().size(); i++) {
            System.out.println((i + 1) + ". " + university.getFaculties().get(i).getFullName());
        }
        System.out.print("Обрати номер факультету: ");
        int num = readInt(1, university.getFaculties().size());
        return university.getFaculties().get(num - 1);
    }

    //department
    private static void departmentMenu() {
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

    private static void createDepartment(Faculty faculty) {
        System.out.print("Назва кафедри: ");
        String name = readNonEmpty();
        System.out.print("Локація: ");
        String location = readNonEmpty();
        Department department = new Department(UUID.randomUUID().toString(), name, location, null);
        faculty.getDepartments().add(department);
        System.out.println("Створено.");
    }

    private static void editDepartment(Faculty faculty) {
        Department dep = selectDepartment(faculty);
        if (dep == null) return;
        System.out.print("Нова назва (поточна: " + dep.getName() + "): ");
        String name = readNonEmpty();
        System.out.print("Нова локація (поточна: " + dep.getLocation() + "): ");
        String location = readNonEmpty();
        dep.setName(name);
        dep.setLocation(location);
        System.out.println("Відредаговано.");
    }

    private static void deleteDepartment(Faculty faculty) {
        Department dep = selectDepartment(faculty);
        if (dep == null) return;
        faculty.getDepartments().remove(dep);
        System.out.println("Видалено.");
    }

    private static Department selectDepartment(Faculty faculty) {
        if (faculty.getDepartments().isEmpty()) {
            System.out.println("Ще немає кафедр.");
            return null;
        }
        for (int i = 0; i < faculty.getDepartments().size(); i++) {
            System.out.println((i + 1) + ". " + faculty.getDepartments().get(i).getName());
        }
        System.out.print("Обрати номер кафедри: ");
        int num = readInt(1, faculty.getDepartments().size());
        return faculty.getDepartments().get(num - 1);
    }

    //student
    private static void studentMenu() {
        Department dep = selectAnyDepartment();
        if (dep == null) return;
        while (true) {
            System.out.println("Меню студентів (кафедра: " + dep.getName() + ")");
            System.out.println("1. Додати студента");
            System.out.println("2. Редагувати студента");
            System.out.println("3. Видалити студента");
            System.out.println("0. Вихід");
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

    private static void addStudent(Department dep) {
        System.out.print("Ім'я: "); String firstName = readNonEmpty();
        System.out.print("Прізвище: "); String lastName = readNonEmpty();
        System.out.print("По батькові: "); String patronymic = readNonEmpty();
        System.out.print("Студентський ID: "); String studentId = readNonEmpty();
        System.out.print("Група (1-6): ");
        int groupNumber = readInt(1, 6);
        String group = String.valueOf(groupNumber);
        System.out.print("Курс (1-6): ");
        int studyYear = readInt(1, 6);
        Student student = new Student(UUID.randomUUID().toString(), firstName, lastName, patronymic, null, "", "", studentId, group, StudyForm.BUDGET, StudentStatus.ACTIVE, studyYear, 2023);
        dep.getStudents().add(student);
        System.out.println("Додано.");
    }

    private static void editStudent(Department dep) {
        Student student = selectStudent(dep);
        if (student == null) return;
        System.out.print("Нове ім'я (поточне: " + student.getFirstName() + "): ");
        student.setFirstName(readNonEmpty());
        System.out.print("Нове прізвище (поточне: " + student.getLastName() + "): ");
        student.setLastName(readNonEmpty());
        System.out.println("Відредаговано.");
    }

    private static void deleteStudent(Department dep) {
        Student student = selectStudent(dep);
        if (student == null) return;
        dep.getStudents().remove(student);
        System.out.println("Видалено.");
    }

    private static Student selectStudent(Department dep) {
        if (dep.getStudents().isEmpty()) {
            System.out.println("Ще немає студентів.");
            return null;
        }
        for (int i = 0; i < dep.getStudents().size(); i++) {
            Student s = dep.getStudents().get(i);
            System.out.println((i + 1) + ". " + s.getFirstName() + " " + s.getLastName() + "");
        }
        System.out.print("Обрати номер студента: ");
        int idx = readInt(1, dep.getStudents().size());
        return dep.getStudents().get(idx - 1);
    }

    private static Department selectAnyDepartment() {
        Faculty faculty = selectFaculty();
        if (faculty == null) return null;
        return selectDepartment(faculty);
    }

    //teacher
    private static void teacherMenu() {
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

    private static void addTeacher(Department dep) {
        System.out.print("Ім'я: ");
        String firstName = readNonEmpty();
        System.out.print("Прізвище: ");
        String lastName = readNonEmpty();
        System.out.print("По батькові: ");
        String patronymic = readNonEmpty();
        System.out.print("Позиція (0-5: ASSISTANT, LECTURER, SENIOR_LECTURER, ASSOCIATE_PROFESSOR, PROFESSOR, HEAD_OF_DEPARTMENT): ");
        Position position = Position.values()[readInt(0, Position.values().length - 1)];
        System.out.print("Науковий ступінь (0-3: BACHELOR, MASTER, PHD, DOCTOR_OF_SCIENCE): ");
        ScientificDegree degree = ScientificDegree.values()[readInt(0, ScientificDegree.values().length - 1)];
        System.out.print("Вчене звання (0-3: NONE, SENIOR_RESEARCHER, DOCENT, PROFESSOR): ");
        AcademicTitle title = AcademicTitle.values()[readInt(0, AcademicTitle.values().length - 1)];
        double rate = 1.0;
        Teacher teacher = new Teacher(UUID.randomUUID().toString(), firstName, lastName, patronymic, null, "", "", position, degree, title, rate);
        dep.getTeachers().add(teacher);
        System.out.println("Додано: " + teacher.getFirstName() + " " + teacher.getLastName());
    }

    private static void editTeacher(Department dep) {
        Teacher teacher = selectTeacher(dep);
        if (teacher == null) return;
        System.out.print("Нове ім'я (поточне: " + teacher.getFirstName() + "): ");
        teacher.setFirstName(readNonEmpty());
        System.out.print("Нове прізвище (поточне: " + teacher.getLastName() + "): ");
        teacher.setLastName(readNonEmpty());
        System.out.print("Нове по батькові (поточне: " + teacher.getPatronymic() + "): ");
        teacher.setPatronymic(readNonEmpty());
        System.out.println("Відредаговано.");
    }

    private static void deleteTeacher(Department dep) {
        Teacher teacher = selectTeacher(dep);
        if (teacher == null) return;
        dep.getTeachers().remove(teacher);
        System.out.println("Видалено: " + teacher.getFirstName() + " " + teacher.getLastName());
    }

    private static Teacher selectTeacher(Department dep) {
        if (dep.getTeachers().isEmpty()) {
            System.out.println("Ще немає викладачів.");
            return null;
        }
        for (int i = 0; i < dep.getTeachers().size(); i++) {
            Teacher t = dep.getTeachers().get(i);
            System.out.println((i + 1) + ". " + t.getFirstName() + " " + t.getLastName());
        }
        System.out.print("Обрати номер викладача: ");
        int idx = readInt(1, dep.getTeachers().size());
        return dep.getTeachers().get(idx - 1);
    }

    //input validation
    private static String readNonEmpty() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty())
                return input;
            System.out.print("Не може бути порожнім: ");
        }
    }

    private static int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max)
                    return val;
                else System.out.print("Обрати в межах " + min + " - " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Невірний вибір. ");
            }
        }
    }
}

