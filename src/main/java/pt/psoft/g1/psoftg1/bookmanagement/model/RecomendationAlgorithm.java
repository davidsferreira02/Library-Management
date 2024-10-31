package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;

import java.util.Collections;
import java.util.List;

@Component("RecomendationAlgorithm")
public class RecomendationAlgorithm implements Recomendation {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> recommendBooks(int numberOfBooks, int genre) {
        List<GenreLendingsDTO> topGenres = genreRepository.findTopGenresByLendings(genre);
        for (GenreLendingsDTO genreLendingsDTO : topGenres) {
            Genre genre1 = genreRepository.findByString(genreLendingsDTO.getGenre()).get();
            List<Book> books = bookRepository.findTopBooksByGenreList(Collections.singletonList(genre1.getGenre()), numberOfBooks);
            return books;
        }
        return null;
    }
}