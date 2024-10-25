package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validIsbn2 = "111111111111";
    private final String validTitle = "Encantos de contar";
    private final String validTitle2 = "Sonhos de Menino";
    private final Author validAuthor1 = new Author("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("Fantasia");
    private final Genre validGenre2 = new Genre("Jogos de Consola");
    private ArrayList<Author> authors = new ArrayList<>();


    @BeforeEach
    void setUp(){
        authors.clear();
    }

    @Test
    void ensureIsbnNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureTitleNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, null, validGenre, authors, null));
    }

    @Test
    void ensureGenreNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureAuthorsNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, validGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor1);
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, null, validGenre, authors, null));
    }




    @Test
    void applyPatchTest()  {
        authors.add(validAuthor1);
        Book book = new Book("9782826012092", "Old Title", "Description", validGenre, authors, "photoURI");
        ReflectionTestUtils.setField(book, "version", 1L);

        UpdateBookRequest updateRequest = Mockito.mock(UpdateBookRequest.class);
        Mockito.when(updateRequest.getTitle()).thenReturn("New Title");
        Mockito.when(updateRequest.getDescription()).thenReturn("New Description");
        Mockito.when(updateRequest.getGenreObj()).thenReturn(validGenre2);
        Mockito.when(updateRequest.getAuthorObjList()).thenReturn(List.of(validAuthor2));
        Mockito.when(updateRequest.getPhotoURI()).thenReturn("newPhotoURI");


        book.applyPatch(1L, updateRequest);

        assertEquals("New Title", book.getTitle().toString());

        assertEquals("New Description", book.getDescription());

        assertEquals(validGenre2, book.getGenre());

        assertEquals(List.of(validAuthor2), book.getAuthors());

        assertEquals("newPhotoURI", book.getPhoto().getPhotoFile());
    }

    @Test
    void removePhotoTest()  {
authors.add(validAuthor1);
        Book book = new Book("9782826012092", "Old Title", "Description", validGenre, authors, "photoURI");
        ReflectionTestUtils.setField(book, "version", 2L);
        assertThrows(ConflictException.class, () -> book.removePhoto(1L));

        ReflectionTestUtils.setField(book, "version", 1L);
        book.removePhoto(1L);
        assertNull(book.getPhoto());


    }

}