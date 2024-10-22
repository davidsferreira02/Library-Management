package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(1)
public class UserBootstrapper implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;
    private final GenreRepository genreRepository;
    private final JdbcTemplate jdbcTemplate;
    private List<String> queriesToExecute = new ArrayList<>();

    @Override
    @Transactional
    public void run(final String... args)  {
        createReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives",
                LocalDate.of(2024, 1, 20), 1, "2000-01-01", "919191919",
                List.of("Fantasia", "Infantil"), "readerPhotoTest.jpg");

        createReader("joao@gmail.com", "Joaoratao!123", "João Ratao",
                LocalDate.of(2024, 3, 20), 2, "1995-06-02", "929292929",
                null, null);

        createReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas",
                LocalDate.of(2024, 1, 20), 3, "2001-12-03", "939393939",
                null, null);

        createReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins",
                LocalDate.of(2024, 3, 20), 4, "2002-03-20", "912345678",
                null, null);

        createReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa",
                LocalDate.of(2024, 1, 20), 5, "2000-06-03", "912355678",
                null, null);

        createReader("luis@gmail.com", "Luismontenegro!123", "Luís Montenegro",
                LocalDate.of(2024, 3, 20), 6, "1999-03-03", "912355678",
                null, null);

        createReader("antonio@gmail.com", "Antoniocosta!123", "António Costa",
                LocalDate.of(2024, 6, 20), 7, "2001-03-03", "912355778",
                null, null);

        createReader("andre@gmail.com", "Andreventura!123", "André Ventura",
                LocalDate.of(2024, 5, 20), 8, "2001-03-03", "912355888",
                null, null);
        createLibrarian();
        executeQueries();
    }
    private void createReader(String email, String password, String name, LocalDate createdAt,
                              int readerNumber, String birthDate, String phoneNumber,
                              List<String> genres, String photo) {
        if (userRepository.findByUsername(email).isEmpty()) {
            final Reader reader = Reader.newReader(email, password, name);
            userRepository.save(reader);

            String dateFormat = LocalDateTime.of(createdAt, LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE dbo.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, reader.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/" + readerNumber);
            if (readerDetails.isEmpty()) {
                List<Genre> interestList = new ArrayList<>();
                if (genres != null) {
                    for (String genre : genres) {
                        genreRepository.findByString(genre).ifPresent(interestList::add);
                    }
                }
                ReaderDetails details = new ReaderDetails(
                        readerNumber,
                        reader,
                        birthDate,
                        phoneNumber,
                        true,
                        true,
                        true,
                        photo,
                        interestList);
                readerRepository.save(details);
            }
        }
    }

    /*private void createReaders() {
        //Reader1 - Manuel
        if (userRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            final Reader manuel = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
            userRepository.save(manuel);

            //String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String dateFormat = LocalDateTime.of(2024,1,20,0,0,0,0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE dbo.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, manuel.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails1= readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
            Optional<Genre> g1 = genreRepository.findByString("Fantasia");
            Optional<Genre> g2 = genreRepository.findByString("Infantil");
            List<Genre> interestList = new ArrayList<>();
            if(g1.isPresent()) {
                interestList.add(g1.get());
            }

            if(g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails1.isEmpty()) {
                ReaderDetails r1 = new ReaderDetails(
                        1,
                        manuel,
                        "2000-01-01",
                        "919191919",
                        true,
                        true,
                        true,
                        "readerPhotoTest.jpg",
                        interestList);
                readerRepository.save(r1);
            }
        }

        //Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            final Reader joao = Reader.newReader("joao@gmail.com", "Joaoratao!123", "João Ratao");
            userRepository.save(joao);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, joao.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            //jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());


            Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
            if (readerDetails2.isEmpty()) {
                ReaderDetails r2 = new ReaderDetails(
                        2,
                        joao,
                        "1995-06-02",
                        "929292929",
                        true,
                        false,
                        false,
                        null,
                        null);
                readerRepository.save(r2);
            }
        }

        //Reader3 - Pedro
        if (userRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            final Reader pedro = Reader.newReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas");
            userRepository.save(pedro);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, pedro.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        3,
                        pedro,
                        "2001-12-03",
                        "939393939",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r3);
            }
        }

        //Reader4 - Catarina
        if (userRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            final Reader catarina = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");
            userRepository.save(catarina);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, catarina.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails(
                        4,
                        catarina,
                        "2002-03-20",
                        "912345678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r4);
            }
        }

        //Reader5 - Marcelo
        if (userRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            final Reader marcelo = Reader.newReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa");
            userRepository.save(marcelo);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, marcelo.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        5,
                        marcelo,
                        "2000-06-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r5);
            }
        }

        //Reader6 - Luís
        if (userRepository.findByUsername("luis@gmail.com").isEmpty()) {
            final Reader luis = Reader.newReader("luis@gmail.com", "Luismontenegro!123", "Luís Montenegro");
            userRepository.save(luis);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, luis.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails(
                        6,
                        luis,
                        "1999-03-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r6);
            }
        }

        //Reader7 - António
        if (userRepository.findByUsername("antonio@gmail.com").isEmpty()) {
            final Reader antonio = Reader.newReader("antonio@gmail.com", "Antoniocosta!123", "António Costa");
            userRepository.save(antonio);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 6, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, antonio.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r7 = new ReaderDetails(
                        7,
                        antonio,
                        "2001-03-03",
                        "912355778",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r7);
            }
        }

        //Reader8 - André
        if (userRepository.findByUsername("andre@gmail.com").isEmpty()) {
            final Reader andre = Reader.newReader("andre@gmail.com", "Andreventura!123", "André Ventura");
            userRepository.save(andre);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 5, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
            String query = String.format("UPDATE dbo.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, andre.getUsername());

            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        8,
                        andre,
                        "2001-03-03",
                        "912355888",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r5);
            }
        }
    }

     */

    private void createLibrarian(){
        // Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            final User maria = Librarian.newLibrarian("maria@gmail.com", "Mariaroberta!123", "Maria Roberta");
            userRepository.save(maria);
        }
    }

    private void executeQueries() {
        for (String query : queriesToExecute) {
            jdbcTemplate.update(query);
        }
    }
}
