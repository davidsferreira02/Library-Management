package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FineTest {

    @Test
    void testFineCreationWithMockLending() {
        // Create a mock Lending object
        Lending mockLending = Mockito.mock(Lending.class);

        // Define the behavior of the mock object
        when(mockLending.getDaysDelayed()).thenReturn(5);
        when(mockLending.getFineValuePerDayInCents()).thenReturn(100);


        Fine fine = new Fine(mockLending);
        // Verify that the getDaysDelayed method was called
        verify(mockLending, Mockito.times(2)).getDaysDelayed();

        // Verify that the getFineValuePerDayInCents method was called
        verify(mockLending).getFineValuePerDayInCents();
    }



}
