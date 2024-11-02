package pt.psoft.g1.psoftg1.bookmanagement.services;


import static org.junit.jupiter.api.Assertions.*;

import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BookServiceIntegrationTest {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthorRepository authorRepository;

    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public BookService bookService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LendingRepository lendingRepository;

    private Lending lending;
    private ReaderDetails readerDetails;
    private Reader reader;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    public void setUp(){
        author = new Author("Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                null);
        authorRepository.save(author);

        genre = new Genre("Género");
        genreRepository.save(genre);

        List<Author> authors = List.of(author);
        book = new Book("9782826012092",
                "O Inspetor Max",
                "conhecido pastor-alemão que trabalha para a Judiciária, vai ser fundamental para resolver um importante caso de uma rede de malfeitores que quer colocar uma bomba num megaconcerto de uma ilustre cantora",
                genre,
                authors,
                null);
        bookRepository.save(book);

        reader = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
        userRepository.save(reader);

        readerDetails = new ReaderDetails(1,
                reader,
                "2000-01-01",
                "919191919",
                true,
                true,
                true,
                null,
                null);
        readerRepository.save(readerDetails);

        // Create and save the lending
        lending = Lending.newBootstrappingLending(book,
                readerDetails,
                LocalDate.now().getYear(),
                999,
                LocalDate.of(LocalDate.now().getYear(), 1,1),
                LocalDate.of(LocalDate.now().getYear(), 1,11),
                15,
                300);
        lendingRepository.save(lending);
    }

    @AfterEach
    public void tearDown(){
        lendingRepository.delete(lending);
        readerRepository.delete(readerDetails);
        userRepository.delete(reader);
        bookRepository.delete(book);
        genreRepository.delete(genre);
        authorRepository.delete(author);
    }

    @Test
    public void testCreate(){
        List<Long> authors = new ArrayList<>();
        authors.add(author.getAuthorNumber());
        Book book1 = bookService.create(new CreateBookRequest("Description1", "Title1", "Género", null, null,authors), "9789722328296");
        assertNotNull(book1);
        assertEquals(book1.getIsbn(), bookRepository.findByIsbn(book1.getIsbn()).get().getIsbn());
        bookRepository.delete(book1);
        assertTrue(bookRepository.findByIsbn(book1.getIsbn()).isEmpty());
    }

    @Test
    public void testUpdate(){
        List<Long> authors = new ArrayList<>();
        authors.add(author.getAuthorNumber());
        List<Author> authorsBook = List.of(author);
        Book newBook = new Book("9789896378905","Title","Description", genre, authorsBook, null);
        bookRepository.save(newBook);
        bookService.update(new UpdateBookRequest(newBook.getIsbn(), "Updated Title", genre.toString(), authors, newBook.getDescription()), newBook.getVersion().toString());
        Book updatedBook = bookRepository.findByIsbn(newBook.getIsbn()).get();
        assertEquals("Updated Title", updatedBook.getTitle().toString());
        bookRepository.delete(updatedBook);
        assertTrue(bookRepository.findByIsbn("9789896378905").isEmpty());
    }

    @Test
    public void testFindByGenre(){
        assertEquals(bookService.findByGenre(genre.getGenre()).get(0).getIsbn(), bookRepository.findByGenre(genre.getGenre()).get(0).getIsbn());
    }

    @Test
    public void testFindByIsbn(){
        assertEquals(bookService.findByIsbn(book.getIsbn()).getIsbn(), bookRepository.findByIsbn(book.getIsbn()).get().getIsbn());
    }

    @Test
    public void testFindByTitle(){
        assertEquals(bookService.findByTitle(book.getTitle().toString()).get(0).getIsbn(), bookRepository.findByTitle(book.getTitle().toString()).get(0).getIsbn());
    }

    @Test
    public void testRemoveBookPhoto(){
        assertThrows(NotFoundException.class, () -> bookService.removeBookPhoto(book.getIsbn(), book.getVersion()));
    }

    @Test
    public void testFindTop5BooksLent(){
        assertEquals(bookService.findTop5BooksLent().size(),1);
    }

    @Test
    public void testSearchBooks(){
        Page page = new Page(1, 5);
        assertEquals(bookService.searchBooks(page, new SearchBooksQuery(book.getTitle().toString(), book.getGenre().toString(),author.getName())).size(),1);
    }

}
