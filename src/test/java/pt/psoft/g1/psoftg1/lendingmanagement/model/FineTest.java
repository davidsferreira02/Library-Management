package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import static org.junit.Assert.assertEquals;

public class FineTest {

    @Test
    void setLending(){
        Fine fine = new Fine();
        Lending lending = new Lending();
        fine.setLending(lending);
        assertEquals(lending, fine.getLending());
    }



}
