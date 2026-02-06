package ua.ukma.edu.domain;

import java.util.*;
import lombok.*;

@Data
@Builder
public class Faculty {
    private String id;
    private String fullName; //назва факультету
    private String shortName; //скорочена назва
    private String contacts; //контактна інформація

    private Teacher dean; //декан факультету

    @Builder.Default
    private List<Department> departments = new ArrayList<>(); //кафедри факультету
}
