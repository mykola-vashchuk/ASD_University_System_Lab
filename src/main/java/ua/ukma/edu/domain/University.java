package ua.ukma.edu.domain;

import java.util.ArrayList;
import java.util.*;

public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private List<Faculty> faculties = new ArrayList<>();

    public University() {
    }
    //Без списку, бо він створиться сам (new ArrayList<>())
    public University(String fullName, String shortName, String city, String address) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }
    //Без списку факультетів
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return Objects.equals(fullName, that.fullName) && Objects.equals(shortName, that.shortName) && Objects.equals(city, that.city) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, shortName, city, address);
    }
    //Без списку факультетів, тільки кількість
    @Override
    public String toString() {
        return "University{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", facultiesCount=" + faculties.size() +
                '}';
    }
}
