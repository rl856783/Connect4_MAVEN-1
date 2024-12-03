package org.example.connect4;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Board board; // A játéktábla
    private final Player player1; // Az első játékos (ember)
    private final Player player2; // A második játékos (gép)
    private Player currentPlayer; // Az aktuális játékos
    private boolean isComputerTurn; // Jelezze, hogy most a gép jön-e
    private final Random random; // Véletlenszám generátor a gép számára
    private static final String SAVE_FILE = "connect4_save.txt"; // Mentési fájl neve

    /**
     * Konstruktor, amely létrehozza a játékot az emberi és a gépi játékos között.
     * @param player1 Az emberi játékos.
     * @param player2 A gépi játékos.
     */
    public Game(Player player1, Player player2) {
        this.board = new Board(); // Új játéktábla létrehozása
        this.player1 = player1; // Az emberi játékos hozzárendelése
        this.player2 = player2; // A gépi játékos hozzárendelése
        this.currentPlayer = player1; // Az emberi játékos kezd
        this.isComputerTurn = false; // Az első körben az ember játszik
        this.random = new Random(); // Véletlenszám generátor inicializálása
    }

    /**
     * Játékos váltása a kör végén.
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == player1) ? player2 : player1;
        this.isComputerTurn = !this.isComputerTurn; // Váltás gép és ember között
    }

    /**
     * Korong elhelyezése a megadott oszlopban.
     * @param column Az oszlop indexe (0-6).
     * @return True, ha sikeres a lépés, különben false.
     */
    public boolean dropDisk(int column) {
        return board.dropDisk(column, currentPlayer.symbol());
    }

    /**
     * A gépi játékos véletlenszerű lépése.
     * @return Az oszlop indexe (0-6), ahova a gép lépni fog.
     */
    public int computerMove() {
        int column;
        do {
            column = random.nextInt(7); // Véletlenszerű oszlop választása
        } while (!board.isColumnAvailable(column)); // Addig próbálkozunk, amíg találunk üres oszlopot
        return column;
    }

    /**
     * Ellenőrzi, hogy a legutóbbi lépés győzelmet eredményezett-e.
     * @param row Az utoljára elhelyezett korong sora.
     * @param col Az utoljára elhelyezett korong oszlopa.
     * @return True, ha győzelem van, különben false.
     */
    public boolean checkWin(int row, int col) {
        char symbol = board.getBoard()[row][col]; // Az aktuális játékos szimbóluma
        return checkDirection(row, col, 1, 0, symbol) || // Vízszintes ellenőrzés
                checkDirection(row, col, 0, 1, symbol) || // Függőleges ellenőrzés
                checkDirection(row, col, 1, 1, symbol) || // Főátló ellenőrzés
                checkDirection(row, col, 1, -1, symbol);   // Mellékátló ellenőrzés
    }

    /**
     * Ellenőrzi, hogy az adott irányban (dx, dy) van-e 4 egymás melletti korong.
     * @param row A kiindulási sor.
     * @param col A kiindulási oszlop.
     * @param dx Az irány sora (0, 1 vagy -1).
     * @param dy Az irány oszlopa (0, 1 vagy -1).
     * @param symbol A játékos szimbóluma.
     * @return True, ha található 4 egymás melletti korong, különben false.
     */
    private boolean checkDirection(int row, int col, int dx, int dy, char symbol) {
        int count = 1; // Az aktuális korongot is beleszámoljuk

        // Előre haladás az adott irányban
        int r = row + dx;
        int c = col + dy;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board.getBoard()[r][c] == symbol) {
            count++;
            r += dx;
            c += dy;
        }

        // Visszafelé haladás az adott irányban
        r = row - dx;
        c = col - dy;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board.getBoard()[r][c] == symbol) {
            count++;
            r -= dx;
            c -= dy;
        }

        return count >= 4; // Ha 4 vagy több azonos szimbólum van egy sorban, nyer
    }

    /**
     * A játék fő folyamata.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        database.addPlayer(player1.name());
        database.addPlayer(player2.name());

        // Mentett játék betöltése
        if (board.hasSavedGame(SAVE_FILE)) {
            System.out.println("Mentett játékállás található. Be akarod tölteni? (i/n)");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("i")) {
                board.loadFromFile(SAVE_FILE); // Betöltés
            } else {
                System.out.println("Üres táblával indul a játék.");
                board.resetBoard();
            }
        } else {
            System.out.println("Nem található mentett állás. Üres táblával indul a játék.");
            board.resetBoard();
        }

        boolean gameWon = false;

        // Játékmenet ciklusa
        while (!gameWon) {
            board.printBoard();
            int column = -1;

            // Gép lépése
            if (isComputerTurn) {
                column = computerMove();
                System.out.println("A gép választotta a " + (char) ('a' + column) + " oszlopot.");
            } else {
                boolean validInput = false;
                while (!validInput) {
                    System.out.println(currentPlayer.name() + ", válassz egy oszlopot (A-G vagy 'mentés'): ");
                    String input = scanner.nextLine().toLowerCase();

                    // Mentés opció kezelése
                    if (input.equals("mentés")) {
                        board.saveToFile(SAVE_FILE); // Mentés fájlba
                        System.out.println("Játékállás mentve a következő fájlba: " + SAVE_FILE);
                        continue;
                    }

                    // Érvényes oszlop választása
                    if (input.length() == 1 && input.charAt(0) >= 'a' && input.charAt(0) <= 'g') {
                        column = input.charAt(0) - 'a';
                        if (board.isColumnAvailable(column)) {
                            validInput = true; // Érvényes választás
                        } else {
                            System.out.println("Ez az oszlop már tele van. Próbálj egy másikat.");
                        }
                    } else {
                        System.out.println("Érvénytelen választás. Kérlek válassz egy oszlopot A-G között.");
                    }
                }
            }

            // Korong elhelyezése és győzelem ellenőrzése
            if (dropDisk(column)) {
                int row = 0;
                while (row < 6 && board.getBoard()[row][column] != currentPlayer.symbol()) {
                    row++;
                }

                if (checkWin(row, column)) {
                    board.printBoard();
                    System.out.println(currentPlayer.name() + " nyert!");
                    database.updateWins(currentPlayer.name());
                    gameWon = true;
                }

                if (!gameWon) {
                    switchPlayer();
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
