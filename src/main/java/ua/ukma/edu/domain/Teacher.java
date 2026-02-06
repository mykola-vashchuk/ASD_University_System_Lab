package ua.ukma.edu.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Teacher extends Person{
    private String position; //посада
    private String degree; //кафедра
    private String academicTitle; //наукове звання
    private LocalDate hireDate; //дата прийому на роботу
    private double salary; //зарплата

    public Teacher(){}

    public Teacher(String position, String degree, String academicTitle, LocalDate hireDate, double salary) {
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public Teacher(String id, String firstName, String lastName, String patronymic, LocalDate birthDate, String email, String phoneNumber, String position, String degree, String academicTitle, LocalDate hireDate, double salary) {
        super(id, firstName, lastName, patronymic, birthDate, email, phoneNumber);
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Double.compare(salary, teacher.salary) == 0 && Objects.equals(position, teacher.position) && Objects.equals(degree, teacher.degree) && Objects.equals(academicTitle, teacher.academicTitle) && Objects.equals(hireDate, teacher.hireDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, degree, academicTitle, hireDate, salary);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "position='" + position + '\'' +
                ", degree='" + degree + '\'' +
                ", academicTitle='" + academicTitle + '\'' +
                ", hireDate=" + hireDate +
                ", salary=" + salary +
                '}';
    }
}
