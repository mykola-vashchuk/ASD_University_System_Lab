package ua.ukma.edu;

import ua.ukma.edu.domain.*;
import ua.ukma.edu.service.ConsoleMenu;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск системи...");

        University naukma = new University("НаУКМА", "NaUKMA", "Kyiv", "Skovorody 2");

        Faculty fi = new Faculty("f1", "Факультет Інформатики", "ФІ", "fi@ukma.edu.ua", new ArrayList<>());

        naukma.getFaculties().add(fi);

        Teacher head = new Teacher(
                UUID.randomUUID().toString(), "Messi", "Ronaldo", "A",
                LocalDate.of(1950, 1, 1), "messi.ronaldo@ukma.edu.ua", "pass",
                Position.HEAD_OF_DEPARTMENT, ScientificDegree.PHD, AcademicTitle.PROFESSOR,
                LocalDate.of(2000, 9, 1), 1.0
        );

        Department ipzDepartament = new Department("d1", "Кафедра Інформатики", "1 корпус", head);
        fi.getDepartments().add(ipzDepartament);

        Student s1 = new Student(
                UUID.randomUUID().toString(), "Ivan", "Petrenko", null, null, null, null,
                "ID-1", "IPZ-1", null, null, 1, 2025
        );
        Student s2 = new Student(
                UUID.randomUUID().toString(), "Oksana", "Boyko", null, null, null, null,
                "ID-2", "IPZ-3", null, null, 3, 2023
        );

        ipzDepartament.getStudents().add(s1);
        ipzDepartament.getStudents().add(s2);

        ConsoleMenu menu = new ConsoleMenu(naukma);
        menu.start();
    }
}