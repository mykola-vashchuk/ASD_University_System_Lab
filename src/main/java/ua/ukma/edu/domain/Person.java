package ua.ukma.edu.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.time.Period;

@Setter
@Getter
public abstract sealed class Person permits Student, Teacher {
    private String id;
    private String firstName;
    private String lastName;
    private String patronymic; //по батькові
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;

    public Person() {}
    public Person(String id, String firstName, String lastName, String patronymic, LocalDate birthDate, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getAge(){
        LocalDate bDate = getBirthDate();
        if(bDate == null) return 0;
        return Period.between(bDate, LocalDate.now()).getYears();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(patronymic, person.patronymic) && Objects.equals(birthDate, person.birthDate) && Objects.equals(email, person.email) && Objects.equals(phoneNumber, person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, birthDate, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + getAge() +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

