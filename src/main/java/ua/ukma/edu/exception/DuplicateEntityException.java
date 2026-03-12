package ua.ukma.edu.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) { super(message); }
}