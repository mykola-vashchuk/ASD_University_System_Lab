package ua.ukma.edu.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends Person{
    private String position; //посада
    private String degree; //кафедра
    private String academicTitle; //наукове звання
    private LocalDate hireDate; //дата прийому на роботу
    private double salary; //зарплата
}
