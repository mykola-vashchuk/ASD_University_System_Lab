package ua.ukma.edu.domain;

import java.util.*;
import lombok.*;

@Data
@Builder
public class Department {
    private String id;
    private String name;
    private String location; // Кабінет/Локація

    private Teacher head;    // Завідувач (посилання на викладача)

    @Builder.Default
    private List<Student> students = new ArrayList<>();

    @Builder.Default
    private List<Teacher> teachers = new ArrayList<>();
}

