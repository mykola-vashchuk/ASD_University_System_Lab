package ua.ukma.edu.domain;

import lombok.*;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;

    @Builder.Default
    private List<Faculty> faculties = new ArrayList<>();
}
