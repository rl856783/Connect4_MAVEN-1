package org.example.connect4;

/**
 * Játékos rekord, amely tartalmazza a játékos nevét, szimbólumát (X vagy O), és színét (Sárga vagy Piros).
 *
 * @param name   A játékos neve
 * @param symbol A játékos szimbóluma (X vagy O)
 * @param color  A játékos színe (Sárga vagy Piros)
 */
public record Player(String name, char symbol, String color) {
    // A record automatikusan biztosítja az equals, hashCode, és toString metódusokat,
    // így további felülírásra általában nincs szükség.
}
