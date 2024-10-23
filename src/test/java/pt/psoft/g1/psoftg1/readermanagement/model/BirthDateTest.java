package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.nio.file.AccessDeniedException;
import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BirthDateTest {

    @Test
    void ensureBirthDateCanBeCreatedWithValidDate() {
        assertDoesNotThrow(() -> new BirthDate(2000, 1, 1));
    }

    @Test
    void ensureBirthDateCanBeCreatedWithValidStringDate() {
        assertDoesNotThrow(() -> new BirthDate("2000-01-01"));
    }

    @Test
    void ensureExceptionIsThrownForInvalidStringDateFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BirthDate("01-01-2000"));
        assertEquals("Provided birth date is not in a valid format. Use yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void ensureCorrectStringRepresentation() {
        BirthDate birthDate = new BirthDate(2000, 1, 1);
        assertEquals("2000-1-1", birthDate.toString());
    }

    @Test
    void ensureGetBirthDateReturnsCorrectDate() {
        BirthDate birthDate = new BirthDate(2000, 1, 1);
        LocalDate expectedDate = LocalDate.of(2000, 1, 1);
        assertEquals(expectedDate, birthDate.getBirthDate());
    }


    @Test
    void ensureBirthDateCanBeCreatedForExactMinimumAge() {
        ReflectionTestUtils.setField(new BirthDate(), "minimumAge", 18);
        LocalDate exactAgeDate = LocalDate.now().minusYears(18);
        assertDoesNotThrow(() -> new BirthDate(exactAgeDate.getYear(), exactAgeDate.getMonthValue(), exactAgeDate.getDayOfMonth()));
    }

    @Test
    void ensureExceptionIsThrownForInvalidMonth() {
        DateTimeException exception = assertThrows(DateTimeException.class, () -> new BirthDate(2000, 13, 1));
        assertEquals("Invalid value for MonthOfYear (valid values 1 - 12): 13", exception.getMessage());
    }

    @Test
    void ensureExceptionIsThrownForInvalidDay() {
        DateTimeException exception = assertThrows(DateTimeException.class, () -> new BirthDate(2000, 1, 32));
        assertEquals("Invalid value for DayOfMonth (valid values 1 - 28/31): 32", exception.getMessage());
    }

    @Test
    void ensureExceptionIsThrownForInvalidUserAge() {
        BirthDate birthDate = new BirthDate(2000, 1, 1);
        ReflectionTestUtils.setField(birthDate, "minimumAge", 18);
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            ReflectionTestUtils.invokeMethod(birthDate, "setBirthDate", 2019, 1, 1);
        });
    }
}


