package ua.ukma.edu.service;


import ua.ukma.edu.domain.*;
import java.util.Scanner;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class ConsoleMenu {
    private University University;
    private Scanner scanner;
    private UniversityService service;

    public ConsoleMenu(University University) {
        this.University = University;
        this.scanner = new Scanner(System.in);
        this.service = new UniversityService(University);
    }

    public void start(){
        while(true){
            System.out.println("\n===НаУКМА МЕНЮ===");
            System.out.println("1. Показати структуру (Read)");
            System.out.println("2. Додати Факультет");
            System.out.println("3. Додати Кафедру");
            System.out.println("4. Додати Студента");
            System.out.println("5. Знайти Студента (Search)");
            System.out.println("6. Звіт за курсами (Report)");
            System.out.println("0. Вихід");
            System.out.println("Your choice > ");

            int choice = readInt("Ваш вибір > ", 0, 6);

            switch (choice) {
                case 1: printStructure(); break;
                case 2: addFaculty(); break;
                case 3: addDepartment(); break;
                case 4: addStudent(); break;
                case 5: searchStudent(); break;
                case 6: printSortedReport(); break;
                case 0: return;
                default: System.out.println("Невірний вибіо!");
            }
        }
    }
    //Валідація вводу
    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if(input != null && !input.trim().isEmpty()) { return input.trim(); }
            System.out.println("Поле не може бути порожнім!");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true){
            System.out.print(prompt);
            if(scanner.hasNextInt()){
                int value = scanner.nextInt();
                scanner.nextLine();
                if(value >= min && value <= max){ return value; }
                System.out.println(" Число має бути від " + min + " до " + max);
            } else {
                String trash = scanner.nextLine();
                System.out.println(trash + "' — це не число. Спробуйте ще раз!");
            }
        }
    }

    private void printStructure() {
        System.out.println("\n--- Структура Універститеиу---");
        System.out.println("Університет: " + University.getShortName());
        if (University.getFaculties().isEmpty()) {System.out.println("(Порожньо)");}

        for  (Faculty faculty : University.getFaculties()) {
            System.out.println(faculty.getFullName() + " (" + faculty.getShortName() + ")");
            for(Department department : faculty.getDepartments() ){
                System.out.println(department.getName());
                for(Student student : department.getStudents() ){
                    System.out.println(student.getLastName() + " " + student.getFirstName() + " ( Курс: " + student.getStudyYear()+" )");
                }
            }
        }
    }

    private void addFaculty() {
        System.out.println("\n--- Новий Факультет ---");
        String name = readString("Повна назва: ");
        String shortName = readString("Абревіатура (напр. ФІ): ");

        Faculty f = new Faculty(UUID.randomUUID().toString(), name, shortName, null, new ArrayList<>());
        University.getFaculties().add(f);
        System.out.println("Факультет створено!");
    }

    private void addDepartment() {
        System.out.println("\n--- Нова Кафедра ---");
        if (University.getFaculties().isEmpty()) {
            System.out.println("Спочатку створіть факультет (п.2).");
            return;
        }

        System.out.println("Виберіть факультет:");
        for (int i = 0; i < University.getFaculties().size(); i++) {
            System.out.println((i + 1) + ". " + University.getFaculties().get(i).getShortName());
        }
        int fIndex = readInt("> ", 1, University.getFaculties().size()) - 1;
        Faculty fac = University.getFaculties().get(fIndex);

        String name = readString("Назва кафедри: ");
        String loc = readString("Розташування: ");

        Department d = new Department(UUID.randomUUID().toString(), name, loc, null);
        fac.getDepartments().add(d);
        System.out.println("Кафедру додано до " + fac.getShortName());
    }

    private void addStudent() {
        System.out.println("\n--- Новий Студент ---");
        if (University.getFaculties().isEmpty()) {
            System.out.println("Немає факультетів!"); return;
        }

        System.out.println("Оберіть факультет:");
        for (int i = 0; i < University.getFaculties().size(); i++) {
            System.out.println((i + 1) + ". " + University.getFaculties().get(i).getShortName());
        }
        int fIndex = readInt("> ", 1, University.getFaculties().size()) - 1;
        Faculty fac = University.getFaculties().get(fIndex);

        if (fac.getDepartments().isEmpty()) {
            System.out.println("На факультеті немає кафедр! Створіть кафедру (п.3)."); return;
        }

        System.out.println("Оберіть кафедру:");
        for (int i = 0; i < fac.getDepartments().size(); i++) {
            System.out.println((i + 1) + ". " + fac.getDepartments().get(i).getName());
        }
        int dIndex = readInt("> ", 1, fac.getDepartments().size()) - 1;
        Department dept = fac.getDepartments().get(dIndex);

        String lastName = readString("Прізвище: ");
        String firstName = readString("Ім'я: ");
        int year = readInt("Курс (1-6): ", 1, 6);
        String group = readString("Група: ");

        Student s = new Student(
                UUID.randomUUID().toString(),
                firstName, lastName, null, null, null, null,
                "ID-" + System.currentTimeMillis(), group, null, null, year, 2025
        );

        dept.getStudents().add(s);
        System.out.println("Студента успішно додано!");
    }

    private void searchStudent() {
        String query = readString("Введіть прізвище: ");
        List<Student> found = service.findStudentByLastName(query);
        if (found.isEmpty()) {
            System.out.println("Нікого не знайдено.");
        } else {
            System.out.println("Знайдено:");
            for (Student s : found) {
                System.out.println(" - " + s.getLastName() + " " + s.getFirstName() + " (" + s.getGroup() + ")");
            }
        }
    }

    private void printSortedReport() {
        System.out.println("\n--- Звіт: Студенти за курсом ---");
        List<Student> sorted = service.getStudentsSortedByYear();
        for (Student s : sorted) {
            System.out.println(s.getStudyYear() + " курс: " + s.getLastName() + " " + s.getFirstName());
        }
    }
}
