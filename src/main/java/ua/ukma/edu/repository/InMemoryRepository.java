package ua.ukma.edu.repository;

import ua.ukma.edu.exception.DuplicateEntityException;
import ua.ukma.edu.exception.EntityNotFoundException;

import java.util.*;

public abstract class InMemoryRepository<T, ID> implements Repository<T, ID> {
    protected final Map<ID, T> storage = new HashMap<>();
    protected abstract ID getId(T entity);

    @Override
    public T save(T entity) {
        ID id = getId(entity);
        if (storage.containsKey(id)) {
            throw new DuplicateEntityException("Entity with id " + id + " already exists");
        }
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public List<T> findAll() { return new ArrayList<>(storage.values()); }

    @Override
    public T update(T entity) {
        ID id = getId(entity);
        if (!storage.containsKey(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        storage.put(id, entity);
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        if (storage.remove(id) == null) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
    }
}