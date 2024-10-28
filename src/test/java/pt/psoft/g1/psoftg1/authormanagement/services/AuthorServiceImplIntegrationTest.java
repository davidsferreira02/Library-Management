package pt.psoft.g1.psoftg1.authormanagement.services;

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
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Autowired
    private PhotoRepository photoRepository;


    @BeforeEach
    public void setUp() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        List<Author> list = new ArrayList<>();
        list.add(alex);

        for (Author a : list) {
            authorRepository.save(a);
        }
    }

    @Test
    public void whenValidId_thenAuthorShouldBeFound() {
        Long id = 1L;
        Optional<Author> found = authorService.findByAuthorNumber(id);
        found.ifPresent(author -> assertThat(author.getId()).isEqualTo(id));
    }

    @Test
    public void findByNameTest() {
        List<Author> authors = authorService.findByName("Alex");
        Assert.assertEquals(1, authors.size());
        Assert.assertEquals("Alex", authors.get(0).getName());
    }

    @Test
    public void createTest() throws IOException {
        CreateAuthorRequest request1 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", null, null);
        Author createdAuthor1 = authorService.create(request1);
        Assert.assertNull(request1.getPhoto());
        Assert.assertNull(request1.getPhotoURI());

        CreateAuthorRequest request2 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", null, "PHOTO");
        Author createdAuthor2 = authorService.create(request2);
        Assert.assertNull(request2.getPhoto());
        Assert.assertNull(request2.getPhotoURI());

        CreateAuthorRequest request3 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes()), null);
        Author createdAuthor3 = authorService.create(request3);
        Assert.assertNull(request3.getPhoto());
        Assert.assertNull(request3.getPhotoURI());

        CreateAuthorRequest request4 = new CreateAuthorRequest("Jo", "O Alex escreveu livros", new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes()), "PHOTO");
        Author createdAuthor4 = authorService.create(request4);
        MockMultipartFile expectedPhoto = new MockMultipartFile("file", "PHOTO.PNG", "image/png", "some-image-content".getBytes());
        Assert.assertArrayEquals(expectedPhoto.getBytes(), request4.getPhoto().getBytes());
        Assert.assertEquals("PHOTO", request4.getPhotoURI());

        Author expectedAuthor = new Author("Jo", "O Alex escreveu livros", "PHOTO");
        Assert.assertEquals(authorService.create(request4).getName(), authorRepository.save(createdAuthor4).getName());
    }

    @Test
    public void findBooksByAuthorNumberTest() {
        Assert.assertEquals(authorService.findBooksByAuthorNumber(1L), bookRepository.findBooksByAuthorNumber(1L));
    }

    @Test
    public void findTopAuthorsByLendingTest() {
        Pageable pageable = PageRequest.of(0, 5);
        Assert.assertEquals(authorService.findTopAuthorByLendings(), authorRepository.findTopAuthorByLendings(pageable).getContent());
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
      Assert.assertNull(updatedAuthor.get().getPhoto());


    }


}