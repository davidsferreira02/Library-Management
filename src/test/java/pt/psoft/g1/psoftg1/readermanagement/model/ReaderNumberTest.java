package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ReaderNumberTest {

    @Test
    void readerNumberIsCorrectlyFormattedWithYearAndNumber() {
        ReaderNumber readerNumber = new ReaderNumber(2023, 123);
        assertEquals("2023/123", readerNumber.toString());
    }
}
