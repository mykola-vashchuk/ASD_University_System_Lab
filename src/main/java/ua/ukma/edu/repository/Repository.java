package ua.ukma.edu.repository;

import ua.ukma.edu.exception.DuplicateEntityException;
import ua.ukma.edu.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity) throws DuplicateEntityException;
    Optional<T> findById(ID id);
    List<T> findAll();
    T update(T entity) throws EntityNotFoundException;
    void deleteById(ID id) throws EntityNotFoundException;
}