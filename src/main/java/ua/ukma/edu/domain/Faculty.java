package ua.ukma.edu.domain;

import java.io.Serializable;
import java.util.*;

public class Faculty implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fullName; //назва факультету
    private String shortName; //скорочена назва
    private String contacts; //контактна інформація
    private Teacher dean; //декан факультету
    private List<Department> departments = new ArrayList<>(); //кафедри факультету
    private List<Student> students = new ArrayList<>();
    public Faculty() {
    }

    public Faculty(String id, String fullName, String shortName, String contacts, List<Department> departments) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.contacts = contacts;
        if (departments != null) {
            this.departments = departments;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Teacher getDean() {
        return dean;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public List<Department> getDepartments() {return departments;}

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    //Тільки за іменем факультету
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(fullName, faculty.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }

    @Override
   public String toString() {
        return "Faculty{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", contacts='" + contacts + '\'' +
                ", dean=" + dean +
                ", departmentsCount=" + departments.size() +
                '}';
    }
}
