package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReaderNumberTest {

    @Test
    void readerNumberIsCorrectlyFormattedWithYearAndNumber() {
        ReaderNumber readerNumber = new ReaderNumber(2023, 123);
        assertEquals("2023/123", readerNumber.toString());
    }

    @Test
    public void testProtectedConstructor() {
        ReaderNumber readerNumber = new ReaderNumber();
        assertNotNull(readerNumber);
    }

}
