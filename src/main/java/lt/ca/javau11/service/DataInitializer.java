package lt.ca.javau11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.repository.GameRepository;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if the database is empty
        if (gameRepository.count() == 0) {
            System.out.println("Seeding database with sample games...");

            // Add sample games
            Game game1 = new Game(
                    "The Witcher 3",
                    "An open-world RPG set in a medieval fantasy world.",
                    "PC, PS4, Xbox One",
                    LocalDate.of(2015, 5, 19),
                    "/images/witcher3.jpg"
            );
            Game game2 = new Game(
                    "The Legend of Zelda: Breath of the Wild",
                    "An action-adventure game set in an open world.",
                    "Switch, Wii U",
                    LocalDate.of(2017, 3, 3),
                    "/images/zelda.jpg"
            );
            Game game3 = new Game(
                    "Cyberpunk 2077",
                    "A cyberpunk-themed RPG with an expansive world.",
                    "PC, PS4, Xbox One",
                    LocalDate.of(2020, 12, 10),
                    "/images/cyberpunk.jpg"
            );

            // Save games to the database
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            System.out.println("Sample games added successfully!");
        } else {
            System.out.println("Database already seeded.");
        }
    }
}