package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@PropertySource({"classpath:config/library.properties"})
class LendingTest {
    private static final ArrayList<Author> authors = new ArrayList<>();
    private static Book book;
    private static ReaderDetails readerDetails;
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    @BeforeAll
    public static void setup(){
        Author author = new Author("Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                null);
        authors.add(author);
        book = new Book("9782826012092",
                "O Inspetor Max",
                "conhecido pastor-alemão que trabalha para a Judiciária, vai ser fundamental para resolver um importante caso de uma rede de malfeitores que quer colocar uma bomba num megaconcerto de uma ilustre cantora",
                new Genre("Romance"),
                authors,
                null);
        readerDetails = new ReaderDetails(1,
                Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives"),
                "2000-01-01",
                "919191919",
                true,
                true,
                true,
                null,
                null);
    }

    @Test
    void ensureBookNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Lending(null, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents));
    }

    @Test
    void ensureReaderNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Lending(book, null, 1, lendingDurationInDays, fineValuePerDayInCents));
    }

    @Test
    void ensureValidReaderNumber(){
        assertThrows(IllegalArgumentException.class, () -> new Lending(book, readerDetails, -1, lendingDurationInDays, fineValuePerDayInCents));
    }

    @Test
    void testSetReturned(){
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        lending.setReturned(0,null);
        assertEquals(LocalDate.now(), lending.getReturnedDate());
        Lending lending2 = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        lending2.setReturned(0,"commentary");
        assertEquals(lending2.getCommentary(), "commentary");
    }


    @Test
    void testGetDaysDelayed() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(0, lending.getDaysDelayed());

        Lending lending2 = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending2, "limitDate", LocalDate.now().minusDays(10));
        ReflectionTestUtils.setField(lending2, "returnedDate", LocalDate.now().minusDays(5));

        assertEquals(5, lending2.getDaysDelayed());
    }

    @Test
    void testGetDaysUntilReturn(){
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(Optional.of(lendingDurationInDays), lending.getDaysUntilReturn());

        Lending lending2 = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending2, "limitDate", LocalDate.now().minusDays(1));
        assertEquals(Optional.empty(), lending2.getDaysUntilReturn());

        Lending lending3 = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending2, "limitDate", LocalDate.now().minusDays(lendingDurationInDays));
        assertEquals(Optional.of(lendingDurationInDays), lending3.getDaysUntilReturn());
    }

    @Test
    void testGetDaysOverDue(){
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(Optional.empty(), lending.getDaysOverdue());

        Lending lending2 = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending2, "limitDate", LocalDate.now().minusDays(5));
        assertEquals(Optional.of(5), lending2.getDaysOverdue());
    }

    @Test
    void testGetTitle() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals("O Inspetor Max", lending.getTitle());
    }

    @Test
    void testGetLendingNumber() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(LocalDate.now().getYear() + "/1", lending.getLendingNumber());
    }

    @Test
    void testGetBook() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(book, lending.getBook());
    }

    @Test
    void testGetReaderDetails() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(readerDetails, lending.getReaderDetails());
    }

    @Test
    void testGetStartDate() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(LocalDate.now(), lending.getStartDate());
    }

    @Test
    void testGetLimitDate() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertEquals(LocalDate.now().plusDays(lendingDurationInDays), lending.getLimitDate());
    }

    @Test
    void testGetReturnedDate() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertNull(lending.getReturnedDate());
    }

    @Test
    public void getFineValueInCents() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);

        ReflectionTestUtils.setField(lending, "limitDate", LocalDate.now().minusDays(1));
        ReflectionTestUtils.setField(lending, "returnedDate", LocalDate.now().minusDays(1));
        assertEquals(Optional.empty(), lending.getFineValueInCents());


        ReflectionTestUtils.setField(lending, "limitDate", LocalDate.now().minusDays(30));
        ReflectionTestUtils.setField(lending, "returnedDate", LocalDate.now()); // Atraso de 30 dias
        assertEquals(Optional.of(fineValuePerDayInCents * 30), lending.getFineValueInCents());
    }

    @Test
    void testDefaultConstructor() {

        Lending lending= new Lending();
        assertNotNull(lending);

    }

    @Test
    void newBootStrapping(){
        // Act
        int year = 2024;
        int seq = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate returnedDate = LocalDate.of(2024, 1, 10);
        int lendingDuration = 14;
        int fineValuePerDayInCents = 100;
        Lending lending = Lending.newBootstrappingLending(
                book, readerDetails, year, seq, startDate, returnedDate, lendingDuration, fineValuePerDayInCents
        );
        LendingNumber lendingNumber = Mockito.mock(LendingNumber.class);
        when(lendingNumber.toString()).thenReturn("2024/1");
        Assertions.assertNotNull(lending);
        Assertions.assertEquals(book, lending.getBook());
        Assertions.assertEquals(readerDetails, lending.getReaderDetails());
        Assertions.assertEquals(lendingNumber.toString(), lending.getLendingNumber());
        Assertions.assertEquals(startDate, lending.getStartDate());
        Assertions.assertEquals(startDate.plusDays(lendingDuration), lending.getLimitDate());
        Assertions.assertEquals(returnedDate, lending.getReturnedDate());
        Assertions.assertEquals(fineValuePerDayInCents, lending.getFineValuePerDayInCents());
    }

    @Test
    void testSetReturnedThrowsIllegalArgumentExceptionWhenBookAlreadyReturned() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending, "returnedDate", LocalDate.now());

        // Act & Assert: Verifica se IllegalArgumentException é lançada
        assertThrows(IllegalArgumentException.class, () -> {
            lending.setReturned(1L, "Book already returned.");
        });
    }

    @Test
    void testSetReturnedThrowsStaleObjectStateExceptionWhenVersionMismatch() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending, "version", 1L);
        long desiredVersion = 2L;


        assertThrows(StaleObjectStateException.class, () -> {
            lending.setReturned(desiredVersion, "Version mismatch.");
        });
    }

    @Test
    void testSetReturnedWithoutExceptions() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending, "version", 1L);
        long desiredVersion = 1L;
        ReflectionTestUtils.setField(lending, "returnedDate", null);


        lending.setReturned(desiredVersion, "Returning book.");
    }

    @Test
    void getVersion(){
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending, "version", 1L);
        assertEquals(1L, lending.getVersion());
    }

    @Test
    public void testNewBootstrappingLending_NullBook_ThrowsIllegalArgumentException() {

        int year = 2024;
        int seq = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate returnedDate = LocalDate.of(2024, 1, 15);
        int lendingDuration = 14;
        int fineValuePerDayInCents = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            Lending.newBootstrappingLending(null, readerDetails, year, seq, startDate, returnedDate, lendingDuration, fineValuePerDayInCents);
        });
    }


    @Test
    public void getGeneratedId(){
        Lending lending = new Lending();
        lending.setGeneratedId("1234");
        assertEquals("1234", lending.getGeneratedId());
}

    @Test
    void testLendingBeforeDueDate() {

        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        assertTrue(lending.getDaysUntilReturn().isPresent());
        assertEquals(Optional.empty(), lending.getDaysOverdue());
    }

    @Test
    void testLendingAfterDueDate() {
        Lending lending = new Lending(book, readerDetails, 1, lendingDurationInDays, fineValuePerDayInCents);
        ReflectionTestUtils.setField(lending, "limitDate", LocalDate.now().minusDays(5));
        assertEquals(Optional.of(5), lending.getDaysOverdue());
        assertEquals(Optional.empty(), lending.getDaysUntilReturn());
    }

    }



