package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;

import java.util.ArrayList;
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
        List<GenreLendingsDTO> topGenres = genreRepository.findTopGenresByLendings(PageRequest.of(0, genre));
        List<Book> recommendedBooks = new ArrayList<>();


        for (GenreLendingsDTO genreLendingsDTO : topGenres) {
            Genre genreEntity = genreRepository.findByString(genreLendingsDTO.getGenre()).orElse(null);

            if (genreEntity != null) {
                List<BookCountDTO> topBooks = bookRepository.findTopBooksByGenre(genreEntity.getGenre(), PageRequest.of(0, numberOfBooks));
                topBooks.forEach(bookCountDTO -> recommendedBooks.add(bookCountDTO.getBook()));
            }
        }

        return recommendedBooks;
    }

}