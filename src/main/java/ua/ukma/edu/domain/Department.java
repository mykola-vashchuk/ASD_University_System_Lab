package ua.ukma.edu.domain;

import java.util.*;

public class Department {
    private String id;
    private String name;
    private String location; // Кабінет/Локація
    private Teacher head;    // Завідувач (посилання на викладача)
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();

    public Department() {
    }
    //Без списків, бо вони створяться самі
    public Department(String id, String name, String location, Teacher head) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.head = head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Teacher getHead() {
        return head;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    //(За назвою)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    //(Без списків)

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", head=" + head +
                ", studentsCount=" + students.size() +
                ", teachersCount=" + teachers.size() +
                '}';
    }
}


