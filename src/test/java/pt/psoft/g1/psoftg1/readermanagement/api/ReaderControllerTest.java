package pt.psoft.g1.psoftg1.readermanagement.api;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.ApiNinjasService;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderController;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderView;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReaderController.class)
@AutoConfigureMockMvc
public class ReaderControllerTest {
    @MockBean
    private ReaderService readerService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReaderViewMapper readerViewMapper;

    @MockBean
    private LendingService lendingService;

    @MockBean
    private LendingViewMapper lendingViewMapper;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private ApiNinjasService apiNinjasService;

    @MockBean
    private BookViewMapper bookViewMapper;

    @MockBean
    private AuthenticationManager authenticationManager;




    @Autowired
    private ReaderController readerController;


    @Test
    public void testGetDataAsReader() {

        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUsername()).thenReturn("reader_username");
        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser, "password");
        SecurityContextHolder.getContext().setAuthentication(auth);


        when(userService.getAuthenticatedUser(auth)).thenReturn(mockUser);


        ReaderDetails mockReaderDetails = Mockito.mock(ReaderDetails.class);
        when(readerService.findByUsername("reader_username")).thenReturn(Optional.of(mockReaderDetails));


        ReaderView mockReaderView = Mockito.mock(ReaderView.class);
        when(readerViewMapper.toReaderView(mockReaderDetails)).thenReturn(mockReaderView);


        ResponseEntity<?> response = readerController.getData(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ReaderView);
    }


    @Test
    public void testGetDataAsLibrarian() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUsername()).thenReturn("librarian_Username");
        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser, "Password123");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.getAuthenticatedUser(auth)).thenReturn(mockUser);

        ReaderDetails mockReaderDetails = Mockito.mock(ReaderDetails.class);
        when(readerService.findByUsername("librarian_Username")).thenReturn(Optional.of(mockReaderDetails));

        List<ReaderDetails> mockReaderDetailsList = List.of(mockReaderDetails);
        when(readerService.findAll()).thenReturn(mockReaderDetailsList);

        ReaderView mockReaderView = new ReaderView();
        List<ReaderView> mockReaderViewList = List.of(mockReaderView);
        when(readerViewMapper.toReaderView(mockReaderDetails)).thenReturn(mockReaderView);
        when(readerViewMapper.toReaderView(mockReaderDetailsList)).thenReturn(mockReaderViewList);

        ResponseEntity<?> response = readerController.getData(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReaderViewList.get(0), response.getBody());
    }

    @Test
    public void testSearchReaders() {

        SearchReadersQuery query = new SearchReadersQuery();

        query.setName("John");

        SearchRequest<SearchReadersQuery> request = new SearchRequest<>();
        request.setQuery(query);
        request.setPage(new Page(1, 10));


        Reader mockReader = Mockito.mock(Reader.class);
        when(mockReader.getName()).thenReturn(new Name("John Doe"));

        ReaderDetails mockReaderDetails = Mockito.mock(ReaderDetails.class);
        when(mockReaderDetails.getReader()).thenReturn(mockReader);

        List<ReaderDetails> mockReaderDetailsList = List.of(mockReaderDetails);
        when(readerService.searchReaders(request.getPage(), query)).thenReturn(mockReaderDetailsList);


        ReaderView mockReaderView = new ReaderView();
        mockReaderView.setFullName("John Doe");
        List<ReaderView> mockReaderViewList = List.of(mockReaderView);
        when(readerViewMapper.toReaderView(mockReaderDetails)).thenReturn(mockReaderView);
        when(readerViewMapper.toReaderView(mockReaderDetailsList)).thenReturn(mockReaderViewList);


        ListResponse<ReaderView> response = readerController.searchReaders(request);


        assertNotNull(response);
        assertNotNull(response.getItems());


        assertFalse(response.getItems().isEmpty());

        response.getItems().forEach(item -> assertTrue(item instanceof ReaderView));
        response.getItems().forEach(item -> assertTrue(item.getFullName().contains("John")));
    }

    @Test
    public void testFindByPhoneNumberWithExistingPhoneNumber() {

        String phoneNumber = "9234567890";


        ReaderDetails mockReaderDetails = Mockito.mock(ReaderDetails.class);
        when(readerService.findByPhoneNumber(phoneNumber)).thenReturn(List.of(mockReaderDetails));


        ReaderView mockReaderView = new ReaderView();
        List<ReaderView> mockReaderViewList = List.of(mockReaderView);
        when(readerViewMapper.toReaderView(mockReaderDetails)).thenReturn(mockReaderView);
        when(readerViewMapper.toReaderView(List.of(mockReaderDetails))).thenReturn(mockReaderViewList);


        ListResponse<ReaderView> response = readerController.findByPhoneNumber(phoneNumber);


        assertNotNull(response);
        assertNotNull(response.getItems());


        assertFalse(response.getItems().isEmpty());
        response.getItems().forEach(item -> assertTrue(item instanceof ReaderView));
    }

    @Test
    public void testFindByPhoneNumberWithNonExistingPhoneNumber() {
        // Define um número de telefone inexistente
        String nonExistentPhoneNumber = "9187654321";

        // Verifica se a exceção NotFoundException é lançada
        assertThrows(NotFoundException.class, () -> {
            readerController.findByPhoneNumber(nonExistentPhoneNumber);
        });
    }
    @Test
    public void testGetTop5ReaderByGenre() {

        String genre = "Science Fiction";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);


        ReaderBookCountDTO mockReaderBookCountDTO = new ReaderBookCountDTO();
        List<ReaderBookCountDTO> mockReaderBookCountDTOList = List.of(mockReaderBookCountDTO);

        ReaderCountView mockReaderCountView = new ReaderCountView();
        List<ReaderCountView> mockReaderCountViewList = List.of(mockReaderCountView);


        when(readerService.findTopByGenre(genre, startDate, endDate)).thenReturn(mockReaderBookCountDTOList);
        when(readerViewMapper.toReaderCountViewList(mockReaderBookCountDTOList)).thenReturn(mockReaderCountViewList);


        ListResponse<ReaderCountView> response = readerController.getTop5ReaderByGenre(genre, startDate, endDate);


        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(mockReaderCountViewList, response.getItems());
    }



}