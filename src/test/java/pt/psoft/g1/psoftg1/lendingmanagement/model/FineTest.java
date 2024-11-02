package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FineTest {

    @Test
    void ExceptionTest(){

        Lending lending= mock(Lending.class);
        when(lending.getDaysDelayed()).thenReturn(-2);
        assertThrows(IllegalArgumentException.class, () ->new Fine(lending));
        when(lending.getDaysDelayed()).thenReturn(0);
        assertThrows(IllegalArgumentException.class, () -> new Fine(lending));

    }

    @Test
    void shouldCalculateFineWhenDaysDelayedIsPositive() {
        Lending lending = mock(Lending.class);


        when(lending.getDaysDelayed()).thenReturn(5); // Atraso de 5 dias
        when(lending.getFineValuePerDayInCents()).thenReturn(100); // Multa de 100 centavos por dia


        Fine fine = new Fine(lending);


        assertEquals(500, fine.getCentsValue());
    }


    @Test
    void testFineConstructorWithValidLending() {

        Lending lendingMock = mock(Lending.class);


        when(lendingMock.getDaysDelayed()).thenReturn(5);
        when(lendingMock.getFineValuePerDayInCents()).thenReturn(100);


        Fine fine = new Fine(lendingMock);


        verify(lendingMock, times(2)).getDaysDelayed();
        verify(lendingMock).getFineValuePerDayInCents();

    }

    @Test
    void testDefaultConstructor() {

        Fine fine = new Fine();
        assertNotNull(fine);

    }
    @Test
    void getLending(){
        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(2);
        Lending lending2= Mockito.mock(Lending.class);
        Mockito.when(lending2.getDaysDelayed()).thenReturn(4);
        Fine fine = new Fine(lending);
        assertEquals(lending, fine.getLending());
        fine.setLending(lending2);
        assertEquals(lending2, fine.getLending());
    }
}
