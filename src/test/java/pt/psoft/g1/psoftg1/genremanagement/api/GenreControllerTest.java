package pt.psoft.g1.psoftg1.genremanagement.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.GetAverageLendingsQuery;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreViewMapper genreViewMapper;

    @Autowired
    private GenreController genreController;

    @Test
    public void testGetTopWithGenres() {

        GenreBookCountDTO mockGenreBookCountDTO = new GenreBookCountDTO();
        List<GenreBookCountDTO> mockList = List.of(mockGenreBookCountDTO);

        GenreBookCountView mockGenreBookCountView = new GenreBookCountView();
        List<GenreBookCountView> mockViewList = List.of(mockGenreBookCountView);


        when(genreService.findTopGenreByBooks()).thenReturn(mockList);
        when(genreViewMapper.toGenreBookCountView(mockList)).thenReturn(mockViewList);


        ListResponse<GenreBookCountView> response = genreController.getTop();


        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals(mockViewList, response.getItems());
    }
    @Test
    public void testGetLendingsPerMonthLastYearByGenreWithData() {

        GenreLendingsPerMonthDTO mockGenreLendingsCountPerMonthDTO = Mockito.mock(GenreLendingsPerMonthDTO.class);
        List<GenreLendingsPerMonthDTO> mockList = List.of(mockGenreLendingsCountPerMonthDTO);

        GenreLendingsCountPerMonthView mockView = Mockito.mock(GenreLendingsCountPerMonthView.class);
        List<GenreLendingsCountPerMonthView> mockViewList = List.of(mockView);


        when(genreService.getLendingsPerMonthLastYearByGenre()).thenReturn(mockList);
        when(genreViewMapper.toGenreLendingsCountPerMonthView(mockList)).thenReturn(mockViewList);

        // Chama o método diretamente no controlador
        ListResponse<GenreLendingsCountPerMonthView> response = genreController.getLendingsPerMonthLastYearByGenre();

        // Verifica se a resposta contém dados
        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals(mockViewList, response.getItems());
    }

    @Test
    public void testGetLendingsAverageDurationPerMonthWithData() {

        String startDate = "2023-01-01";
        String endDate = "2023-12-31";


        GenreLendingsPerMonthDTO mockGenreLendingsAvgPerMonthDTO = Mockito.mock(GenreLendingsPerMonthDTO.class);
        List<GenreLendingsPerMonthDTO> mockList = List.of(mockGenreLendingsAvgPerMonthDTO);

        GenreLendingsAvgPerMonthView mockView = Mockito.mock(GenreLendingsAvgPerMonthView.class);
        List<GenreLendingsAvgPerMonthView> mockViewList = List.of(mockView);


        when(genreService.getLendingsAverageDurationPerMonth(startDate, endDate)).thenReturn(mockList);
        when(genreViewMapper.toGenreLendingsAveragePerMonthView(mockList)).thenReturn(mockViewList);


        ListResponse<GenreLendingsAvgPerMonthView> response = genreController.getLendingsAverageDurationPerMonth(startDate, endDate);

        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals(mockViewList, response.getItems());
    }
    @Test
    public void testGetAverageLendingsWithData() {

        GetAverageLendingsQuery averageLendingsQuery = new GetAverageLendingsQuery();


        SearchRequest<GetAverageLendingsQuery> request = new SearchRequest<>();
        request.setQuery(averageLendingsQuery);
        request.setPage(new Page(1,10));


        GenreLendingsDTO mockGenreLendingsDTO = Mockito.mock(GenreLendingsDTO.class);
        List<GenreLendingsDTO> mockList = List.of(mockGenreLendingsDTO);

        GenreLendingsView mockView = Mockito.mock(GenreLendingsView.class);
        List<GenreLendingsView> mockViewList = List.of(mockView);


        when(genreService.getAverageLendings(averageLendingsQuery, new Page(1,10))).thenReturn(mockList);
        when(genreViewMapper.toGenreAvgLendingsView(mockList)).thenReturn(mockViewList);

        ListResponse<GenreLendingsView> response = genreController.getAverageLendings(request);


        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals(mockViewList, response.getItems());
    }

}