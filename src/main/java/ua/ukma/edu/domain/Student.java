package ua.ukma.edu.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Student extends Person{
    private String studentId; //студентський квиток
    private String group; //група
    private StudyForm studyForm; //форма навчання
    private StudentStatus studentStatus; //статус студента
    private int studyYear; //курс навчання
    private int addmisionYear; //рік вступу
}
