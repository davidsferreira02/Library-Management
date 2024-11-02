package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FineTest {

    @Test
    void ExceptionTest(){

        Lending lending= mock(Lending.class);
        when(lending.getDaysDelayed()).thenReturn(-2);
        assertThrows(IllegalArgumentException.class, () ->new Fine(lending));

    }
/*
    @Test
    void getPk(){
        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(2);
        Fine fine = new Fine(lending);
        fine.getPk();
    }
    @Test
    void getFineValuePerDaysInCents(){
        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(2);
        Fine fine = new Fine(lending);
        fine.getFineValuePerDaysInCents();
    }
    @Test
    void getLending(){
        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(2);
        Fine fine = new Fine(lending);
        fine.getLending();
    }
    @Test
    void getCentsValue(){
        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(2);
        Fine fine = new Fine(lending);
        fine.getCentsValue();
    }*/

    @Test
    void testFineConstructorWithValidLending() {
        // Cria um mock para a classe Lending
        Lending lendingMock = mock(Lending.class);

        // Define o comportamento do mock para simular um empréstimo com atraso
        when(lendingMock.getDaysDelayed()).thenReturn(5);
        when(lendingMock.getFineValuePerDayInCents()).thenReturn(100);

        // Cria uma nova instância de Fine usando o mock de Lending
        Fine fine = new Fine(lendingMock);

        // Verifica que os métodos getDaysDelayed e getFineValuePerDayInCents foram chamados
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
        Assert.assertEquals(lending, fine.getLending());
        fine.setLending(lending2);
        Assert.assertEquals(lending2, fine.getLending());
    }
}
