package org.example.connect4;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:connect4.db"; // Adatbázis elérési útvonala

    /**
     * Kapcsolódás az SQLite adatbázishoz.
     * @return Connection objektum, amely az adatbázis kapcsolatot képviseli.
     * @throws SQLException ha a kapcsolódás sikertelen.
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL); // Kapcsolódás az SQLite adatbázishoz
    }

    /**
     * Játékos hozzáadása az adatbázishoz. Ha a játékos már létezik, nem történik módosítás.
     * @param name A játékos neve.
     */
    public void addPlayer(String name) {
        // Ellenőrzi, hogy a játékos már létezik-e az adatbázisban
        String checkQuery = "SELECT COUNT(*) FROM players WHERE name = ?";
        try (Connection connection = connect();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, name); // Játékos nevének beállítása a lekérdezéshez
            ResultSet resultSet = checkStatement.executeQuery(); // Lekérdezés végrehajtása
            if (resultSet.getInt(1) > 0) { // Ha a játékos már létezik
                System.out.println("A játékos már létezik: " + name);
                return; // Nem adja hozzá újra a játékost
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Új játékos hozzáadása, ha még nem létezik
        String query = "INSERT INTO players (name, wins) VALUES (?, 0)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name); // Játékos neve
            statement.executeUpdate(); // Adatok mentése az adatbázisba
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A játékosok magas pontszámainak (győzelmeinek) listázása.
     * Az eredmények győzelmek szerinti csökkenő sorrendben jelennek meg.
     */
    public void printHighScores() {
        String query = "SELECT * FROM players ORDER BY wins DESC"; // Lekérdezés a játékosok listázására

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Highscore:"); // Highscore táblázat címe
            while (resultSet.next()) { // Minden sor bejárása az eredményhalmazban
                String name = resultSet.getString("name"); // Játékos neve
                int wins = resultSet.getInt("wins"); // Győzelmek száma
                System.out.println(name + " - " + wins + " győzelem"); // Eredmények kiírása
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Egy játékos győzelmeinek frissítése. Ha a játékos nem létezik, automatikusan hozzáadódik.
     * @param playerName A frissítendő játékos neve.
     */
    public void updateWins(String playerName) {
        String query = "UPDATE players SET wins = wins + 1 WHERE name = ?"; // Frissítés lekérdezése

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playerName); // Játékos neve
            int rowsAffected = statement.executeUpdate(); // Sorok frissítése az adatbázisban
            if (rowsAffected == 0) { // Ha nincs ilyen játékos, új játékos hozzáadása
                System.out.println("Nem található ilyen játékos, új játékos hozzáadása...");
                addPlayer(playerName); // Új játékos hozzáadása
            } else {
                System.out.println(playerName + " győzelme frissítve."); // Sikeres frissítés
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Az adatbázisban egy `players` nevű tábla létrehozása, ha az még nem létezik.
     * A tábla az alábbi mezőket tartalmazza:
     * - id: Egyedi azonosító (INTEGER, automatikusan növekvő).
     * - name: Játékos neve (TEXT).
     * - wins: Győzelmek száma (INTEGER).
     */
    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "wins INTEGER NOT NULL)";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query); // Tábla létrehozása, ha még nem létezett
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
