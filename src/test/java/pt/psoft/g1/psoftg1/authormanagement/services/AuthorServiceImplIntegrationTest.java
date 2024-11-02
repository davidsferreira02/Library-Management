package pt.psoft.g1.psoftg1.authormanagement.services;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorServiceImplIntegrationTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;




    @BeforeEach
    public void setUp() {;
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        List<Author> list = new ArrayList<>();
        list.add(alex);

        for (Author a : list) {
            authorRepository.save(a);
        }
    }

    @Test
    void testFindAll() {
        Iterable<Author> authors = authorService.findAll();
        Assertions.assertThat((Iterable<Author>) authors).hasSize(1);
        Assertions.assertThat((Iterable<Author>) authors).extracting(Author::getName)
                .containsExactlyInAnyOrder("Alex");
    }

    @Test
    public void whenValidId_thenAuthorShouldBeFound() {
        long id = 1L;
        Optional<Author> found = authorService.findByAuthorNumber(id);
        found.ifPresent(author -> assertThat(author.getId()).isEqualTo(id));
    }

    @Test
    public void findByNameTest() {
        Author david = new Author("David", "O david escreveu livros", null);
        authorRepository.save(david);
        List<Author> authors = authorService.findByName("David");
        assertEquals(1, authors.size());
        assertEquals("David", authors.get(0).getName());
    }

    @Test
    public void createTest() throws IOException {
        CreateAuthorRequest request1 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", null, null);
        Author createdAuthor1 = authorService.create(request1);
        assertNull(request1.getPhoto());
        assertNull(request1.getPhotoURI());

        CreateAuthorRequest request2 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", null, "PHOTO");
        Author createdAuthor2 = authorService.create(request2);
        assertNull(request2.getPhoto());
        assertNull(request2.getPhotoURI());

        CreateAuthorRequest request3 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes()), null);
        Author createdAuthor3 = authorService.create(request3);
        assertNull(request3.getPhoto());
        assertNull(request3.getPhotoURI());

        CreateAuthorRequest request4 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes()), "PHOTO");
        Author createdAuthor4 = authorService.create(request4);
        MockMultipartFile expectedPhoto = new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes());
        Assert.assertArrayEquals(expectedPhoto.getBytes(), request4.getPhoto().getBytes());
        assertEquals("PHOTO", request4.getPhotoURI());

        Author expectedAuthor = new Author("Jo", "O Alex escreveu livros", "PHOTO");
        assertEquals(authorService.create(request4).getName(), authorRepository.save(createdAuthor4).getName());
    }

    @Test
    public void findBooksByAuthorNumberTest() {
        assertEquals(authorService.findBooksByAuthorNumber(1L), bookRepository.findBooksByAuthorNumber(1L));
    }

    @Test
    public void findTopAuthorsByLendingTest() {
        Pageable pageable = PageRequest.of(0, 5);
        assertEquals(authorService.findTopAuthorByLendings(), authorRepository.findTopAuthorByLendings(pageable).getContent());
    }

    @Test
    public void removeAuthorPhotoTest() {
        // Setup: Create and save an author with a photo
        Author author = new Author("Author1", "Bio1", "photoFile1");
        authorRepository.save(author);

        // Verify the photo exists before removal
        Assert.assertNotNull(author.getPhoto());

        // Execute: Call the method to remove the photo
        Optional<Author> updatedAuthor = authorService.removeAuthorPhoto(author.getAuthorNumber(), author.getVersion());
      assertNull(updatedAuthor.get().getPhoto());


    }


    @Test
    void partialUpdate(){


        Author author = new Author("Author1", "Bio1", "photoFile1");
        authorRepository.save(author);
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        updateRequest.setName("New Author Name");

        assertThrows(NotFoundException.class, () -> {
            authorService.partialUpdate(999L, updateRequest, 1L);
        });

        MultipartFile photo = new MockMultipartFile("photo.jpg", new byte[] {1, 2, 3});
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhoto(photo);
        request.setPhotoURI(null);

        Author updatedAuthor = authorService.partialUpdate(author.getAuthorNumber(), request, author.getVersion());


        assertNull(request.getPhotoURI());


        request.setPhoto(null);
        request.setPhotoURI("valid-uri");

        updatedAuthor = authorService.partialUpdate(author.getAuthorNumber(), request, author.getVersion());

        assertNull(request.getPhotoURI());


        request.setPhoto(null);
        request.setPhotoURI(null);

        updatedAuthor = authorService.partialUpdate(author.getAuthorNumber(), request, author.getVersion());

        assertNull(request.getPhotoURI());

        request.setPhoto(photo);
        request.setPhotoURI("valid-uri");

        updatedAuthor = authorService.partialUpdate(author.getAuthorNumber(), request, author.getVersion());

        assertEquals("valid-uri", updatedAuthor.getPhoto().getPhotoFile());

        author.applyPatch(author.getVersion(), request);

        Assert.assertTrue(authorRepository.findByAuthorNumber(author.getAuthorNumber()).isPresent());

    }


}