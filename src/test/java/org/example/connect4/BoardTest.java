
package org.example.connect4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testDropDisk() {
        Assertions.assertTrue(board.dropDisk(0, 'X')); // Üres oszlopba sikeres elhelyezés
        Assertions.assertEquals('X', board.getBoard()[5][0]); // Korong az alsó sorban
    }

    @Test
    void testIsColumnAvailable() {
        Assertions.assertTrue(board.isColumnAvailable(0)); // Kezdetben elérhető az oszlop
        board.dropDisk(0, 'X');
        Assertions.assertTrue(board.isColumnAvailable(0)); // Egy koronggal még mindig elérhető
    }

    @Test
    void testResetBoard() {
        board.dropDisk(0, 'X');
        board.resetBoard();
        for (char[] row : board.getBoard()) {
            for (char cell : row) {
                Assertions.assertEquals(' ', cell); // Minden mező üres
            }
        }
    }
}
