package com.example.tictactoegame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TictactoegameTest {
    @Test
    public void testWrongInputWhenNull() {
        testWrongInput(null);
    }

    @Test
    public void testWrongInputWhenDimensionSizeMismatch() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'X', 'O', 'X'},
            {'X', 'O', 'X', ' '}
        };
        testWrongInput(board);
    }

    @Test
    public void testWrongInputWhenDimensionsCountMismatch() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'X', 'O', ' '}
        };
        testWrongInput(board);
    }

    @Test
    public void testWrongInputWhenRowIsNull() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            null,
            null
        };
        testWrongInput(board);
    }

    @Test
    public void testWrongInputWhenWrongSymbolOnTheBoard() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'O', '-', 'O'},
            {'X', 'O', 'X'}
        };
        testWrongInput(board);
    }

    @Test
    public void testWrongInputWhenSecondPlayerMadeMoreMovesThanFirst() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'O', 'O', 'O'},
            {'X', 'O', 'X'}
        };
        testWrongInput(board);
    }

    private void testWrongInput(char[][] board) {
        Tictactoegame instance = new Tictactoegame();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            instance.getWinner(board);
        });
        assertNotEquals(null, exception, "Exception has not been thrown");
    
        String expectedMessage = Tictactoegame.WRONG_MOVES_INPUT;
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage), actualMessage);
    }


    @Test
    public void testFirstPlayerWinsByRow() {
        char[][] board = new char[][]{
            {'X', 'X', 'X'},
            {'O', ' ', 'O'},
            {' ', 'O', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.FIRST_PLAYER, result, "Wrong result, when row is filled by first player");
    }
    
    @Test
    public void testFirstPlayerWinsByColumn() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'X', ' ', 'O'},
            {'X', 'O', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.FIRST_PLAYER, result, "Wrong result, when column is filled by first player");
    }

    @Test
    public void testFirstPlayerWinsByLeftDiagonal() {
        char[][] board = new char[][]{
            {'X', 'O', ' '},
            {' ', 'X', 'O'},
            {' ', 'O', 'X'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.FIRST_PLAYER, result, "Wrong result, when left diagonal is filled by first player");
    }

    @Test
    public void testFirstPlayerWinsByRightDiagonal() {
        char[][] board = new char[][]{
            {' ', 'O', 'X'},
            {' ', 'X', 'O'},
            {'X', 'O', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.FIRST_PLAYER, result, "Wrong result, when right diagonal is filled by first player");
    }
    
    @Test
    public void testSecondPlayerWinsByRow() {
        char[][] board = new char[][]{
            {'X', ' ', 'X'},
            {'O', 'O', 'O'},
            {'X', ' ', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.SECOND_PLAYER, result, "Wrong result, when row is filled by second player");
    }
    
    @Test
    public void testSecondPlayerWinsByColumn() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {' ', 'O', ' '},
            {'X', 'O', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.SECOND_PLAYER, result, "Wrong result, when column is filled by second player");
    }

    @Test
    public void testSecondPlayerWinsByLeftDiagonal() {
        char[][] board = new char[][]{
            {'O', ' ', 'X'},
            {'X', 'O', ' '},
            {'X', ' ', 'O'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.SECOND_PLAYER, result, "Wrong result, when left diagonal is filled by second player");
    }

    @Test
    public void testSecondPlayerWinsByRightDiagonal() {
        char[][] board = new char[][]{
            {'X', ' ', 'O'},
            {'X', 'O', ' '},
            {'O', ' ', 'X'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.SECOND_PLAYER, result, "Wrong result, when right diagonal is filled by second player");
    }

    @Test
    public void testImpossiblePosition() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'X', 'O', 'O'},
            {'X', 'O', 'X'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.IMPOSSIBLE_POSITION, result, "Wrong result, when such position is impossible");
    }

    @Test
    public void testNotEnoughMoves() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'O', ' ', 'O'},
            {'X', 'O', 'X'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.NOT_ENOUGH_MOVES, result, "Wrong result, when not enough moves are done");
    }

    @Test
    public void testNotEnoughMovesWhenBoardEmpty() {
        char[][] board = new char[][]{
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.NOT_ENOUGH_MOVES, result, "Wrong result, when not enough moves are done - board is empty");
    }

    @Test
    public void testDraw() {
        char[][] board = new char[][]{
            {'X', 'O', 'X'},
            {'X', 'O', 'O'},
            {'O', 'X', 'X'}
        };
        Tictactoegame instance = new Tictactoegame();
        Tictactoegame.GameResult result = instance.getWinner(board);
        assertEquals(Tictactoegame.GameResult.DRAW, result, "Wrong result, when board is filled, and no one won");
    }
}
