package ua.ukma.edu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.ukma.edu.domain.University;

import static org.junit.jupiter.api.Assertions.*;

public class UniversityTests {
    @Test
    void testUniversityCreation() {
        University university = new University(
                "Національний університет Києво-Могилянська академія",
                "НаУКМА",
                "Київ",
                "Григорія Сковороди 2"
        );

        assertEquals("НаУКМА", university.getShortName());
        assertEquals("Національний університет Києво-Могилянська академія", university.getFullName());
    }

    @Test
    void testUniversityAddresses() {
        University university = new University(
                "НаУКМА",
                "НаУКМА",
                "Київ",
                "Григорія Сковороди 2"
        );

        assertEquals("Київ", university.getCity());
        assertEquals("Григорія Сковороди 2", university.getAddress());
    }

    @Test
    void testUniversityEquals() {
        University uni1 = new University("НаУКМА", "НаУКМА", "Київ", "Адреса 1");
        University uni2 = new University("НаУКМА", "НаУКМА", "Київ", "Адреса 1");

        assertEquals(uni1, uni2);
    }

    @Test
    void testUniversityNotEquals() {
        University uni1 = new University("НаУКМА", "НаУКМА", "Київ", "Адреса 1");
        University uni2 = new University("КНУ", "КНУ", "Київ", "Адреса 2");

        assertNotEquals(uni1, uni2);
    }

    @Test
    void testUniversityHashCode() {
        University uni1 = new University("НаУКМА", "НаУКМА", "Київ", "Адреса");
        University uni2 = new University("НаУКМА", "НаУКМА", "Київ", "Адреса");

        assertEquals(uni1.hashCode(), uni2.hashCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"НаУКМА", "КНУ", "НТУУ", "УкрНАУ"})
    void testUniversityShortNameNotEmpty(String shortName) {
        University university = new University("Full Name", shortName, "City", "Address");
        assertNotNull(university.getShortName());
        assertFalse(university.getShortName().isEmpty());
    }
}

