package ua.ukma.edu.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.time.Period;

public class Teacher extends Person {
    private Position position;//посада
    private ScientificDegree degree;//науковий ступінь
    private AcademicTitle academicTitle;//вчене звання
    private LocalDate hireDate;//дата прийому на роботу
    private double rate;//ставка/навантаження (наприклад, 1.0 - повна ставка, 0.5 - половина ставки)

    public Teacher() {
    }

    public Teacher(Position position, ScientificDegree degree, AcademicTitle academicTitle, LocalDate hireDate, double rate) {
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.rate = rate;
    }

    public Teacher(String id, String firstName, String lastName, String patronymic, LocalDate birthDate, String email, String phoneNumber, Position position, ScientificDegree degree, AcademicTitle academicTitle, LocalDate hireDate, double rate) {
        super(id, firstName, lastName, patronymic, birthDate, email, phoneNumber);
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.rate = rate;
    }

    public Teacher(String string, String firstName, String lastName, String patronymic, Object o, String s, String s1, Position position, ScientificDegree degree, AcademicTitle title, double rate) {
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ScientificDegree getDegree() {
        return degree;
    }

    public void setDegree(ScientificDegree degree) {
        this.degree = degree;
    }

    public AcademicTitle getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(AcademicTitle academicTitle) {
        this.academicTitle = academicTitle;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getExperienceYear(){
        if (this.hireDate == null) return 0;
        return Period.between(this.hireDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "position=" + position +
                ", degree=" + degree +
                ", academicTitle=" + academicTitle +
                ", hireDate=" + hireDate +
                ", experience=" + getExperienceYear() +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Double.compare(teacher.rate, rate) == 0 &&
                position == teacher.position &&
                degree == teacher.degree &&
                academicTitle == teacher.academicTitle &&
                Objects.equals(hireDate, teacher.hireDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, degree, academicTitle, hireDate, rate);
    }

}
