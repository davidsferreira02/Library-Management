package pt.psoft.g1.psoftg1.authormanagement.repository;
/*
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = "pt.psoft.g1.psoftg1")
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;

    private Author author;
    private Author author2;
    private Book book;
    private Genre genre;

    @BeforeEach
    public void setUp(){
        author = new Author("Author1", "Bio24", null);
        authorRepository.save(author);
        author2 = new Author("Author2", "Bio24", null);
        authorRepository.save(author2);
        genre = new Genre("Género");
        genreRepository.save(genre);
        List<Author> authors = List.of(author, author2);
        book = new Book("9782826012092",
                "O Inspetor Max",
                "conhecido pastor-alemão que trabalha para a Judiciária, vai ser fundamental para resolver um importante caso de uma rede de malfeitores que quer colocar uma bomba num megaconcerto de uma ilustre cantora",
                genre,
                authors,
                null);
        bookRepository.save(book);
    }

    @AfterEach
    public void tearDown(){
        authorRepository.delete(author);
        authorRepository.delete(author2);
        genreRepository.delete(genre);
        bookRepository.delete(book);
    }

    @Test
    public void whenFindByName_thenReturnAuthor() {
        // given
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<Author> list = authorRepository.searchByNameName(alex.getName());

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName())
                .isEqualTo(alex.getName());
    }

    @Test
    public void testSearchByNameNameStartsWith(){
        List<Author> authorList = authorRepository.searchByNameNameStartsWith("Author1");
        assertNotNull(authorList);
        assertEquals(authorList.get(0).getName(), author.getName(), "Author name should be" + author.getName());
    }

    @Test
    public void testSaveDelete(){
        Author testAuthor = new Author("Test1", "Test1", null);
        authorRepository.save(testAuthor);
        assertEquals(authorRepository.searchByNameName("Test1").get(0).getName(), testAuthor.getName());
        authorRepository.delete(testAuthor);
        assertEquals(authorRepository.searchByNameName("Test1").size(), 0);
    }

    @Test
    public void testFindCoAuthorsByAuthorNumber(){
        List<Author> authorList = authorRepository.findCoAuthorsByAuthorNumber(author.getAuthorNumber());
        assertEquals(authorList.get(0).getAuthorNumber(), author2.getAuthorNumber());
    }

}*/