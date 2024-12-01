
package org.example.connect4;
import java.util.Scanner;

public class Connect4 {

    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Emberi játékos nevének bekérése
        System.out.print("Add meg a játékos nevét: ");
        String playerName = scanner.nextLine(); // A név beolvasása a konzolról.

        // Emberi játékos létrehozása: 'X' szimbólumot és sárga színt kap.
        Player player1 = new Player(playerName, 'X', "\u001B[33m");

        // Gépi játékos létrehozása: 'O' szimbólumot és piros színt kap.
        Player player2 = new Player("Gép", 'O', "\u001B[31m");

        // Játék inicializálása az emberi és gépi játékossal.
        Game game = new Game(player1, player2);

        // A játék indítása.
        game.startGame();

        // Adatbázis kapcsolat inicializálása.
        Database database = new Database();

        // Adatbázis tábla létrehozása, ha még nem létezik (pl. highscore tárolására).
        database.createTable();

        // A játékos nevének mentése az adatbázisba.
        database.addPlayer(playerName);

        // Kérdés, hogy megjelenítsük-e a highscore táblázatot.
        System.out.print("Szeretnéd megjeleníteni a highscore táblázatot? (igen/nem): ");
        String showHighScores = scanner.nextLine().toLowerCase();  // Felhasználói válasz beolvasása.

        // Ha a válasz "igen", megjelenítjük a highscore táblázatot.
        if ("igen".equals(showHighScores)) {
            database.printHighScores(); // Highscore adatok megjelenítése.
        } else {
            System.out.println("A játék véget ért."); // Egyéb esetben a játék véget ér.
        }

        // A Scanner bezárása, mivel nem használjuk tovább (ha nem zárta le máshol a kód).
        scanner.close();
    }
}
