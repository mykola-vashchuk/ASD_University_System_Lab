package ua.ukma.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.ukma.edu.domain.University;
import ua.ukma.edu.persistence.UniversityStorage;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UniversityStorageTests {
    private UniversityStorage storage;
    private University testUniversity;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        Path filePath = tempDir.resolve("test-university.bin");
        storage = new UniversityStorage(filePath);
        testUniversity = new University(
                "Тестовий Університет",
                "ТУ",
                "Київ",
                "Вул. Тестова, 1"
        );
    }

    @Test
    public void testSaveAndLoad() {
        storage.save(testUniversity);
        Optional<University> loaded = storage.load();

        assertTrue(loaded.isPresent());
        assertEquals("ТУ", loaded.get().getShortName());
        assertEquals("Тестовий Університет", loaded.get().getFullName());
    }

    @Test
    public void testLoadNonExistent() {
        Optional<University> loaded = storage.load();
        assertTrue(loaded.isEmpty());
    }

    @Test
    public void testLoadOrDefault() {
        University result = storage.loadOrDefault(() -> testUniversity);
        assertNotNull(result);
        assertEquals("ТУ", result.getShortName());
    }

    @Test
    public void testSaveNullUniversity() {
        assertThrows(NullPointerException.class, () -> storage.save(null));
    }

    @Test
    public void testUpdateExistingUniversity() {
        storage.save(testUniversity);

        testUniversity.setShortName("НТУ");
        storage.save(testUniversity);

        Optional<University> loaded = storage.load();
        assertTrue(loaded.isPresent());
        assertEquals("НТУ", loaded.get().getShortName());
    }
}

