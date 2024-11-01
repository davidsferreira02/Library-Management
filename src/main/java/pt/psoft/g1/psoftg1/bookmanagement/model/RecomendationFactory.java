package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;

@Component
public class RecomendationFactory {

    private Recomendation recomendation;

    @Value("${RecomendationAlgorithm}")
    private String algorithmName;

    @Value("${numberOfBooks}")
    private int numberOfBooks;

    @Value("${numberOfGenres}")
    private int genre;

    private final ApplicationContext applicationContext;

    @Autowired
    public RecomendationFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        this.recomendation = applicationContext.getBean(algorithmName, Recomendation.class);
    }

    public List<Book> generateRecommendation(ReaderDetails readerDetails) {
        return recomendation.recommendBooks(readerDetails, numberOfBooks, genre);
    }
}