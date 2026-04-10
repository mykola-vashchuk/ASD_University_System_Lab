package ua.ukma.edu.persistence;

import ua.ukma.edu.domain.University;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class UniversityStorage {
    private final Path storagePath;

    public UniversityStorage() {
        this(Paths.get("data", "university.bin"));
    }

    public UniversityStorage(Path storagePath) {
        this.storagePath = Objects.requireNonNull(storagePath, "Шлях до файлу збереження не може бути null.");
    }

    public Optional<University> load() {
        if (!Files.exists(storagePath)) return Optional.empty();

        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(storagePath))) {
            return Optional.of((University) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Не вдалося завантажити дані з файлу: " + storagePath, e);
        }
    }

    public University loadOrDefault(Supplier<University> defaultUniversitySupplier) {
        Objects.requireNonNull(defaultUniversitySupplier, "Постачальник університету не може бути null.");
        return load().orElseGet(() -> {
            University university = Objects.requireNonNull(defaultUniversitySupplier.get(), "Університет не може бути null.");
            save(university);
            return university;
        });
    }

    public void save(University university) {
        Objects.requireNonNull(university, "Університет не може бути null.");
        try {
            Path parent = storagePath.getParent();
            if (parent != null) Files.createDirectories(parent);

            Path tempFile = parent == null
                    ? Files.createTempFile("university-", ".bin.tmp")
                    : Files.createTempFile(parent, "university-", ".bin.tmp");

            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(tempFile))) {
                outputStream.writeObject(university);
            }

            try {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Не вдалося зберегти дані у файл: " + storagePath, e);
        }
    }
}

