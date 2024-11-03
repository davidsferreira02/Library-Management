package pt.psoft.g1.psoftg1.bookmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.*;
import pt.psoft.g1.psoftg1.genremanagement.api.*;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.GetAverageLendingsQuery;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderView;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BookControllerTest {

    @MockBean
    private BookService bookService;
    @MockBean
    private LendingService lendingService;
    @MockBean
    private ConcurrencyService concurrencyService;
    @MockBean
    private FileStorageService fileStorageService;
    @MockBean
    private UserService userService;
    @MockBean
    private ReaderService readerService;

    @MockBean
    private BookViewMapper bookViewMapper;

    @Autowired
    private BookController bookController;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        Author author = new Author("Author1", "Bio1", "photoFile1");
        Genre genre = new Genre("Fantasy");
        List<Author> authors = List.of(author);
        List<Long> authorsNumbers = new ArrayList<>();
        authorsNumbers.add(author.getAuthorNumber());
        Book book = new Book("9782826012092","Test Book"," ", genre, authors,null);
        book.setVersion(1L);
        CreateBookRequest request = new CreateBookRequest("", "Test Book", "Fantasy", null, null,authorsNumbers);
        BookView bookView = new BookView();
        bookView.setTitle("Test Book");
        bookView.setIsbn("9782826012092");

        when(bookService.create(any(CreateBookRequest.class), anyString())).thenReturn(book);
        when(bookViewMapper.toBookView(any(Book.class))).thenReturn(bookView);
        when(fileStorageService.getRequestPhoto(any())).thenReturn(null);

        ResponseEntity<BookView> response = bookController.create(request, "9782826012092");

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getTitle());
        assertEquals("9782826012092", response.getBody().getIsbn());
    }

    @Test
    void testFindByIsbn() {
        String isbn = "9782826012092";
        Book book = new Book(isbn, "Test Book", " ", new Genre("Fantasy"), List.of(new Author("Author1", "Bio1", "photoFile1")), null);
        book.setVersion(1L);
        BookView bookView = new BookView();
        bookView.setTitle("Test Book");
        bookView.setIsbn(isbn);

        when(bookService.findByIsbn(isbn)).thenReturn(book);
        when(bookViewMapper.toBookView(book)).thenReturn(bookView);

        ResponseEntity<BookView> response = bookController.findByIsbn(isbn);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getTitle());
        assertEquals(isbn, response.getBody().getIsbn());
    }



}