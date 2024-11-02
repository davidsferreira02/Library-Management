package pt.psoft.g1.psoftg1.bookmanagement.service;




   import org.junit.Assert;
   import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
   import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
   import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
    @SpringBootTest
    @Transactional
    public class BookServiceIntegrationTest {

        @Autowired
        private BookService bookService;

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private AuthorRepository authorRepository;

        @Autowired
        private GenreRepository genreRepository;

        @BeforeEach
        public void setUp() {
            // Ensure the genre exists
            Genre genre = new Genre("Fantasy");
            genreRepository.save(genre);

            // Ensure the authors exist
            Author author1 = new Author("Author1", "Bio1", "photoFile1");
            Author author2 = new Author("Author2", "Bio2", "photoFile2");
            authorRepository.save(author1);
            authorRepository.save(author2);
        }

        @Test
        public void testCreateBookSuccessfully() {
            // Preparação dos dados
            String isbn = "9780306406157";
            CreateBookRequest request = new CreateBookRequest();
            request.setTitle("New Book Title");
            request.setDescription("This is a description of the new book.");
            request.setGenre("Fantasy");
            request.setPhotoURI("photoFile");
            request.setPhoto(null);
            request.setAuthors(List.of(1L, 2L));

            Book createdBook = bookService.create(request, isbn);

            Assert.assertNull(request.getPhoto());
            Assert.assertNull(request.getPhotoURI());
            assertNotNull(createdBook);
            assertEquals(isbn, createdBook.getIsbn());
            assertEquals(request.getTitle().toString(), createdBook.getTitle().toString());
            assertEquals(request.getDescription(), createdBook.getDescription());
            assertEquals("Fantasy", createdBook.getGenre().toString());
            assertEquals(2, createdBook.getAuthors().size());
        }


    }


 */