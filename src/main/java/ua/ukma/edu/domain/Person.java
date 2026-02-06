package ua.ukma.edu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String patronymic; //по батькові
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
}
