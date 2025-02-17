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
            Game game1 = new Game("Cyberpunk 2077", "A cyberpunk-themed RPG with an expansive world.", "PC, PS4, Xbox One", LocalDate.of(2020, 12, 10), "/images/cyberpunk.jpg");
            Game game2 = new Game("Elden Ring", "A challenging open-world RPG by FromSoftware.", "PC, PS4, PS5, Xbox One, Xbox Series X/S", LocalDate.of(2022, 2, 25), "/images/eldenring.jpg");
            Game game3 = new Game("God of War", "A mythological action-adventure game set in Norse mythology.", "PS4, PS5, PC", LocalDate.of(2018, 4, 20), "/images/godofwar.jpg");
            Game game4 = new Game("Hades", "A rogue-like dungeon crawler where you fight through the Underworld.", "PC, Switch, PS4, PS5, Xbox One, Xbox Series X/S", LocalDate.of(2020, 9, 17), "/images/hades.jpg");
            Game game5 = new Game("Red Dead Redemption 2", "An epic Western adventure with stunning visuals.", "PC, PS4, Xbox One", LocalDate.of(2018, 10, 26), "/images/rdr2.jpg");
            Game game6 = new Game("Resident Evil Village", "A survival horror game with intense action and suspense.", "PC, PS4, PS5, Xbox One, Xbox Series X/S", LocalDate.of(2021, 5, 7), "/images/revillage.jpg");
            Game game7 = new Game("Star Wars Jedi: Fallen Order", "A Star Wars action-adventure game featuring a Jedi in hiding.", "PC, PS4, PS5, Xbox One, Xbox Series X/S", LocalDate.of(2019, 11, 15), "/images/jedi.jpg");
            Game game8 = new Game("Super Mario Odyssey", "A platforming adventure with Mario exploring various worlds.", "Switch", LocalDate.of(2017, 10, 27), "/images/mario.jpg");
            Game game9 = new Game("The Witcher 3", "An open-world RPG set in a medieval fantasy world.", "PC, PS4, Xbox One", LocalDate.of(2015, 5, 19), "/images/witcher3.jpg");
            Game game10 = new Game("The Legend of Zelda: Breath of the Wild", "An action-adventure game set in an open world.", "Switch, Wii U", LocalDate.of(2017, 3, 3), "/images/zelda.jpg");

            // Save games to the database
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);
            gameRepository.save(game4);
            gameRepository.save(game5);
            gameRepository.save(game6);
            gameRepository.save(game7);
            gameRepository.save(game8);
            gameRepository.save(game9);
            gameRepository.save(game10);

            System.out.println("Sample games added successfully!");
        } else {
            System.out.println("Database already seeded.");
        }
    }
}
