package ua.ukma.edu.repository;

import ua.ukma.edu.domain.Department;
import java.util.*;

public class DepartmentRepository extends InMemoryRepository<Department, String> {
    @Override
    protected String getId(Department entity) { return entity.getId(); }
}