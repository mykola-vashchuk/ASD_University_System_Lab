package ua.ukma.edu;
import org.junit.jupiter.api.Test;
import ua.ukma.edu.domain.University;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    @Test
    void uniNameCorrect() {
        University university = new University("Національний університет Києво-Могилянська академія", "НаУКМА", "Київ", "Григорія Сковороди 2");
        assertEquals("НаУКМА", university.getShortName());

    }

    @Test
    void uniAddressNotNull() {
        University university = new University("Національний університет Києво-Могилянська академія", "НаУКМА", "Київ", "Григорія Сковороди 2");
        assertNotNull(university.getAddress());

    }

    @Test
    void uniNamesEqual() {
        University university1 = new University("Національний університет Києво-Могилянська академія");
        University university2 = new University("Національний університет Києво-Могилянська академія");

        assertEquals(university1.getShortName(), university2.getFullName());
    }

    @Test
    void uniCityNotEmpty() {
        University university = new University("Національний університет Києво-Могилянська академія", "НаУКМА", "Київ", "Григорія Сковороди 2");
        assertFalse(university.getCity().isEmpty());
    }

    @Test
    void uniNamesDifferent() {
        University university1 = new University("Національний університет Києво-Могилянська академія", "НаУКМА", "Київ", "Григорія Сковороди 2");
        University university2 = new University("КНУ");

        assertNotEquals(university1.getShortName(), university2.getShortName());
    }
}