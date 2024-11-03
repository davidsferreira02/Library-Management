package pt.psoft.g1.psoftg1.authormanagement.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.Transient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private AuthorController authorController;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorViewMapper authorViewMapper;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private BookViewMapper bookViewMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;


    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // Any setup if needed; otherwise, MockBeans are automatically initialized.
    }

    @Test
    void testCreate() {
        CreateAuthorRequest request = new CreateAuthorRequest();
        request.setPhoto(null); // Assuming the photo is null for simplicity

        Author author = new Author("Author1", "Bio24", null);
        author.setVersion(1L);

        AuthorView authorView = new AuthorView();

        when(fileStorageService.getRequestPhoto(any(MultipartFile.class))).thenReturn(null);
        when(authorService.create(any(CreateAuthorRequest.class))).thenReturn(author);
        when(authorViewMapper.toAuthorView(any(Author.class))).thenReturn(authorView);

        ResponseEntity<AuthorView> response = authorController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(authorView, response.getBody());
    }

    @Test
    void testPartialUpdate() {
        long authorNumber = 1L;
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhoto(null);

        Author author = new Author("Author1", "Bio24", null);
        author.setVersion(1L);

        AuthorView authorView = new AuthorView();
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader(ConcurrencyService.IF_MATCH)).thenReturn("1");

        when(fileStorageService.getRequestPhoto(any(MultipartFile.class))).thenReturn(null);
        when(authorService.partialUpdate(eq(authorNumber), any(UpdateAuthorRequest.class), anyLong())).thenReturn(author);
        when(authorViewMapper.toAuthorView(any(Author.class))).thenReturn(authorView);

        ResponseEntity<AuthorView> response = authorController.partialUpdate(authorNumber, webRequest, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(authorView, response.getBody());
    }

    @Test
    void testFindByAuthorNumber() {
        long authorNumber = 1L;
        Author author = new Author("Author1", "Bio24", null);
        author.setVersion(1L);

        AuthorView authorView = new AuthorView();

        when(authorService.findByAuthorNumber(authorNumber)).thenReturn(Optional.of(author));
        when(authorViewMapper.toAuthorView(author)).thenReturn(authorView);

        ResponseEntity<AuthorView> response = authorController.findByAuthorNumber(authorNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(authorView, response.getBody());
    }
}

