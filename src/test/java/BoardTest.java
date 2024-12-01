/**
package org.example.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testDropDisk() {
        assertTrue(board.dropDisk(0, 'X')); // Üres oszlopba sikeres elhelyezés
        assertEquals('X', board.getBoard()[5][0]); // Korong az alsó sorban
    }

    @Test
    void testIsColumnAvailable() {
        assertTrue(board.isColumnAvailable(0)); // Kezdetben elérhető az oszlop
        board.dropDisk(0, 'X');
        assertTrue(board.isColumnAvailable(0)); // Egy koronggal még mindig elérhető
    }

    @Test
    void testResetBoard() {
        board.dropDisk(0, 'X');
        board.resetBoard();
        for (char[] row : board.getBoard()) {
            for (char cell : row) {
                assertEquals(' ', cell); // Minden mező üres
            }
        }
    }
}
*/