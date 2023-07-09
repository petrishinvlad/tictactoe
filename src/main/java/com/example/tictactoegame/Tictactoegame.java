package com.example.tictactoegame;

import java.util.*;

/**
 * Emulates the TicTacToe game, based on provided moves
 * All coordinates on the board are assigned in the following way:
 * [0,0], [0,1], [0,2] 
 * [1,0], [1,1], [1,2] 
 * [2,0], [2,1], [2,2] 
 * The size of the board can be changed by BOARD_DIMENSION_SIZE constant
 * Based on the game board, we are looking for the winner on the specific board
 * if not enough moves - "Not enough moves"
 * first player winner - "A"
 * second player winner - "B"
 * no winner - "Draw"
 * GameState is the wrapper for holding game state
 * We assume, that only 2 players can play TicTacToe. 
 * In this case, we can calculate score by incrementing(first player) and decrementing(second player) number of selected squares in row/column/diagonal
 * 
 * @author  Vladyslav Petryshyn
 * @since 1.2
 */
public class Tictactoegame {
    private static final int BOARD_DIMENSION_SIZE = 3;
    private static final int LINE_SIZE = 3;
    private static final int FIRST_PLAYER_INDEX = 0;
    private static final int SECOND_PLAYER_INDEX = 1;
    private static final int EMPTY_SYMBOL_INDEX = 2;
    private static final int LEFT_DIAGONAL_INDEX = 0;
    private static final int RIGHT_DIAGONAL_INDEX = 1;
    private static final char FIRST_PLAYER_SYMBOL = 'X';
    private static final char SECOND_PLAYER_SYMBOL = 'O';
    private static final char EMPTY_SQUARE_SYMBOL = ' ';
    private static final List<Character> LEGAL_SYMBOLS = Arrays.asList(FIRST_PLAYER_SYMBOL, SECOND_PLAYER_SYMBOL, EMPTY_SQUARE_SYMBOL);
    private static final List<Character> PLAYER_SYMBOLS = Arrays.asList(FIRST_PLAYER_SYMBOL, SECOND_PLAYER_SYMBOL);
    protected static final String WRONG_MOVES_INPUT = "Wrong input";

    /**
     * In case the number of the players will be changed - new enum values should be introduced
     */
    enum GameResult {
        FIRST_PLAYER, 
        SECOND_PLAYER, 
        DRAW, 
        NOT_ENOUGH_MOVES,
        IMPOSSIBLE_POSITION;
    }

    /**
     * GameState is the wrapper for the processed game information
     * playersMoveCount array contains info about how many moves each player did
     * i.e. playersMoveCount[0] = 3 => first player did 3 moves,
     * playersMoveCount[1] = 2 => second player did 2 moves
     * rowsCount/colsCount - ????
     * 
     * playerWinningLaneFound determines, whether winning condition for a player is in effect
     * i.e. playerWinningLaneFound[1] = true => second player filled the line of LINE_SIZE size
     * 
     * diagonalsCount contains data about filling diagonal lines by each player
     * diagonalsCount[playerIndex] - contains player's data about left(index 0) and right(index 1) diagonals
     * i.e. diagonalsCount[0][1] = 2 => first player filled 2 squares on the right diagonal
     * in case the parameters for board/line/players number would change - implementation should be changed
     */
    class GameState {
        private int[] playersMoveCount = new int[2];
        private int[][] rowsCount = new int[BOARD_DIMENSION_SIZE][2];
        private int[][] colsCount = new int[BOARD_DIMENSION_SIZE][2];
        private boolean[] playerWinningLaneFound = new boolean[2];
        private int[][] diagonalsCount = new int[2][2];
    }

    /**
     * @param moves - 2-dimensional array of moves, that are done by both players(one by one).
     * 1st dimension is a move index, 2nd - board dimension index(0 - row, 1 - column) 
     * @return GameResult. Possible options are:
     * - FIRST_PLAYER 
     * - SECOND_PLAYER 
     * - DRAW 
     * - NOT_ENOUGH_MOVES
     * @throws IllegalArgumentException
     */
    public GameResult getWinner(final char[][] board) {
        GameState gameState = new GameState();
        validateBoard(board, gameState);
        for (int row = 0; row < BOARD_DIMENSION_SIZE; ++row) {
            for (int col = 0; col < BOARD_DIMENSION_SIZE; ++col) {
                checkRow(board, row, col, gameState);
                checkColumn(board, row, col, gameState);
                checkDiagonal(board, row, col, gameState);
            }
        }
        if (allPlayersFilledLanes(gameState)) {
            return GameResult.IMPOSSIBLE_POSITION;
        } else if (gameState.playerWinningLaneFound[FIRST_PLAYER_INDEX]) {
            return GameResult.FIRST_PLAYER;
        } else if (gameState.playerWinningLaneFound[SECOND_PLAYER_INDEX]) {
            return GameResult.SECOND_PLAYER;
        } else if (allSquaresFilledOnTheBoard(gameState)) {
            return GameResult.DRAW; 
        }
        return GameResult.NOT_ENOUGH_MOVES;
    }

    private boolean allPlayersFilledLanes(final GameState gameState) {
        for (int playerIndex = 0; playerIndex < gameState.playerWinningLaneFound.length; ++playerIndex) {
            if (!gameState.playerWinningLaneFound[playerIndex]) {
                return false;
            }
        }
        return true;
    }

    private boolean allSquaresFilledOnTheBoard(final GameState gameState) {
        int numberOfBoardSquares = BOARD_DIMENSION_SIZE * BOARD_DIMENSION_SIZE;
        int numberOfFilledBoardSquares = gameState.playersMoveCount[FIRST_PLAYER_INDEX] + gameState.playersMoveCount[SECOND_PLAYER_INDEX];
        return numberOfFilledBoardSquares == numberOfBoardSquares;
    }

    private void checkColumn(final char[][] board, final int row, final int col, final GameState gameState) {
        checkStraightLine(board, row, col, gameState, false);
    }

    private void checkRow(final char[][] board, final int row, final int col, final GameState gameState) {
        checkStraightLine(board, row, col, gameState, true);
    }

    private void checkStraightLine(final char[][] board, final int row, final int col, final GameState gameState, final boolean isRow) {
        int[][] countArray = isRow ? gameState.rowsCount : gameState.colsCount;
        int lineTypeToCheck = isRow ? row : col;
        int symbolTypeIndex = getSymbolIndex(board, row, col);
        if (countArray[lineTypeToCheck][0] == symbolTypeIndex) {
            countArray[lineTypeToCheck][1] += 1;
        } else {
            countArray[lineTypeToCheck][0] = symbolTypeIndex;
            countArray[lineTypeToCheck][1] = 1;
        }
        if (countArray[lineTypeToCheck][1] == LINE_SIZE 
            && countArray[lineTypeToCheck][0] != EMPTY_SYMBOL_INDEX) {
            gameState.playerWinningLaneFound[symbolTypeIndex] = true;
        }
    }

    private void checkDiagonal(final char[][] board, final int row, final int col, final GameState gameState) {
        int symbolTypeIndex = getSymbolIndex(board, row, col);
        if (symbolTypeIndex != EMPTY_SYMBOL_INDEX) {
            int playerIndex = symbolTypeIndex;   
            if (row == col) {
                gameState.diagonalsCount[playerIndex][LEFT_DIAGONAL_INDEX] += 1;
                if (gameState.diagonalsCount[playerIndex][LEFT_DIAGONAL_INDEX] == LINE_SIZE) {
                    gameState.playerWinningLaneFound[playerIndex] = true;
                }
            }
            if (row == BOARD_DIMENSION_SIZE - 1 - col) {
                gameState.diagonalsCount[playerIndex][RIGHT_DIAGONAL_INDEX] += 1;
                if (gameState.diagonalsCount[playerIndex][RIGHT_DIAGONAL_INDEX] == LINE_SIZE) {
                    gameState.playerWinningLaneFound[playerIndex] = true;
                }
            }
        }
    }

    private int getSymbolIndex(final char[][] board, final int row, final int col) {
        return LEGAL_SYMBOLS.indexOf(board[row][col]);
    }

    private void validateBoard(final char[][] board, final GameState gameState) {
        if (board == null || board.length != BOARD_DIMENSION_SIZE) throw new IllegalArgumentException(WRONG_MOVES_INPUT);
        for (char[] row: board) {
            if (row == null || row.length != BOARD_DIMENSION_SIZE) {
                throw new IllegalArgumentException(WRONG_MOVES_INPUT);
            }
            for (char square: row) {
                if (!LEGAL_SYMBOLS.contains(square)) {
                    throw new IllegalArgumentException(WRONG_MOVES_INPUT);
                }
                if (PLAYER_SYMBOLS.contains(square)) {
                    int playerIndex = PLAYER_SYMBOLS.indexOf(square);
                    gameState.playersMoveCount[playerIndex] += 1;
                }
            }
        }
        for (int playerIndex = 0; playerIndex < gameState.playersMoveCount.length - 1; ++playerIndex) {
            if (gameState.playersMoveCount[playerIndex] < gameState.playersMoveCount[playerIndex + 1]) {
                throw new IllegalArgumentException(WRONG_MOVES_INPUT);
            }
        }
    }
}