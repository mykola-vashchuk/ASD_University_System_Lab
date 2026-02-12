package ua.ukma.edu.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person{
    private String studentId; //студентський квиток
    private String group; //група
    private StudyForm studyForm; //форма навчання
    private StudentStatus studentStatus; //статус студента
    private int studyYear; //курс навчання
    private int admissionYear; //рік вступу

    public Student(){}

    public Student(String studentId, String group, StudyForm studyForm, StudentStatus studentStatus, int studyYear, int admissionYear) {
        this.studentId = studentId;
        this.group = group;
        this.studyForm = studyForm;
        this.studentStatus = studentStatus;
        this.studyYear = studyYear;
        this.admissionYear = admissionYear;
    }

    public Student(String id, String firstName, String lastName, String patronymic, LocalDate birthDate, String email, String phoneNumber, String studentId, String group, StudyForm studyForm, StudentStatus studentStatus, int studyYear, int admissionYear) {
        super(id, firstName, lastName, patronymic, birthDate, email, phoneNumber);
        this.studentId = studentId;
        this.group = group;
        this.studyForm = studyForm;
        this.studentStatus = studentStatus;
        this.studyYear = studyYear;
        this.admissionYear = admissionYear;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public StudyForm getStudyForm() {
        return studyForm;
    }

    public void setStudyForm(StudyForm studyForm) {
        this.studyForm = studyForm;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return studyYear == student.studyYear && admissionYear == student.admissionYear && Objects.equals(studentId, student.studentId) && Objects.equals(group, student.group) && studyForm == student.studyForm && studentStatus == student.studentStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId, group, studyForm, studentStatus, studyYear, admissionYear);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", group='" + group + '\'' +
                ", studyForm=" + studyForm +
                ", studentStatus=" + studentStatus +
                ", studyYear=" + studyYear +
                ", admissionYear=" + admissionYear +
                '}';
    }
}
