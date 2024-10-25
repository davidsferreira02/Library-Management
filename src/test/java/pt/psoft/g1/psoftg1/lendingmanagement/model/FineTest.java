package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FineTest {

    @Test
    void ExceptionTest(){

        Lending lending= Mockito.mock(Lending.class);
        Mockito.when(lending.getDaysDelayed()).thenReturn(-2);
        assertThrows(IllegalArgumentException.class, () ->new Fine(lending));

    }
}
