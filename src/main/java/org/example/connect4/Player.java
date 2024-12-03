package org.example.connect4;

/**
 * Játékos rekord, amely tartalmazza a játékos nevét, szimbólumát (X vagy O), és színét (Sárga vagy Piros).
 *
 * @param name   A játékos neve
 * @param symbol A játékos szimbóluma (X vagy O)
 * @param color  A játékos színe (Sárga vagy Piros)
 */
public record Player(String name, char symbol, String color) {
    // Egyedi toString implementáció
    @Override
    public String toString() {
        return "Player{name='" + name + "', symbol='" + symbol + "', color='" + color + "'}";
    }
}

