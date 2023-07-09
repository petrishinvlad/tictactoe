package com.example.tictactoegame;

import java.util.*;

/**
 * Emulates the TicTacToe game, based on provided board
 * The size of the board can be changed by BOARD_DIMENSION_SIZE constant
 * Based on the game board, we are looking for the winner on the specific board
 * if not enough moves are done - "Not enough moves"
 * first player winner - "A"
 * second player winner - "B"
 * no winner - "Draw"
 * if both players filled the lanes - "Impossible position"
 * GameState is the wrapper for holding game state
 * 
 * @author  Vladyslav Petryshyn
 * @since 1.2
 */
public class Tictactoegame {
    private static final int BOARD_DIMENSION_SIZE = 3;
    private static final int LINE_SIZE = 3;
    private static final int FIRST_PLAYER_INDEX = 0;
    private static final int SECOND_PLAYER_INDEX = 1;
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

    enum LineType {
        ROW, COLUMN, LEFT_DIAGONAL, RIGHT_DIAGONAL
    }

    /**
     * GameState is the wrapper for the processed game information
     * playersMoveCount array contains info about how many moves each player did
     * i.e. playersMoveCount[0] = 3 => first player did 3 moves,
     * playersMoveCount[1] = 2 => second player did 2 moves
     * rowsCount/colsCount contains info about the moves, done by players on the specific row
     * if rowsCount[i] == 3/-3 => 1st/2nd player filled the row/column
     * diagonalsCount contains 2 values => diagonalsCount[0] for left diagonal, diagonalsCount[1] for right
     * 
     * playerWinningLaneFound determines, whether winning condition for a player is in effect
     * i.e. playerWinningLaneFound[1] = true => second player filled the line
     */
    class GameState {
        private int[] playersMoveCount = new int[2];
        private int[] rowsCount = new int[BOARD_DIMENSION_SIZE];
        private int[] colsCount = new int[BOARD_DIMENSION_SIZE];
        private boolean[] playerWinningLaneFound = new boolean[2];
        private int[] diagonalsCount = new int[2];

        private GameState(final char[][] board) {
            validateBoard(board);
            for (int row = 0; row < BOARD_DIMENSION_SIZE; ++row) {
                for (int col = 0; col < BOARD_DIMENSION_SIZE; ++col) {
                    for (LineType lineType: LineType.values()) {
                        checkLine(board, row, col, lineType);
                    }
                }
            }
        }
    
        private void checkLine(final char[][] board, final int row, final int col, 
                                final LineType lineType) {
            int increment = getSymbolIncrement(board, row, col);
            if (increment != 0) {
                int[] countArray = getStatsArray(lineType);
                int statsIndex = getStatsIndex(lineType, row, col);
                if (statsIndex != -1) {
                    addSymbolToStats(countArray, statsIndex, increment);
                }
            }
        }

        private int[] getStatsArray(final LineType lineType) {
            return lineType == LineType.ROW ? rowsCount : 
                    lineType == LineType.COLUMN ? colsCount : 
                    diagonalsCount;
        }

        private int getStatsIndex(final LineType lineType, final int row, final int col) {
            return lineType == LineType.ROW ? row : 
                    lineType == LineType.COLUMN ? col : 
                    lineType == LineType.LEFT_DIAGONAL && row == col ? LEFT_DIAGONAL_INDEX :
                    lineType == LineType.RIGHT_DIAGONAL && row == BOARD_DIMENSION_SIZE - 1 - col ? RIGHT_DIAGONAL_INDEX : -1;
        }

        private int getSymbolIncrement(final char[][] board, final int row, final int col) {
            return board[row][col] == SECOND_PLAYER_SYMBOL ? -1 : 
                    board[row][col] == FIRST_PLAYER_SYMBOL ? 1 : 0;
        }

        private void addSymbolToStats(final int[] statsData, final int index, final int symbolIncrement) {
            statsData[index] += symbolIncrement;
            if (statsData[index] == LINE_SIZE * symbolIncrement) {
                int player = symbolIncrement == 1 ? FIRST_PLAYER_INDEX : SECOND_PLAYER_INDEX;
                playerWinningLaneFound[player] = true;
            }
        }

        private void validateBoard(final char[][] board) {
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
                        playersMoveCount[playerIndex] += 1;
                    }
                }
            }
            if (playersMoveCount[FIRST_PLAYER_INDEX] != playersMoveCount[SECOND_PLAYER_INDEX]
                && playersMoveCount[FIRST_PLAYER_INDEX] != playersMoveCount[SECOND_PLAYER_INDEX] + 1) {
                throw new IllegalArgumentException(WRONG_MOVES_INPUT);
            }
        }

        private boolean bothPlayersFilledLanes() {
            return playerWinningLaneFound[FIRST_PLAYER_INDEX] && playerWinningLaneFound[SECOND_PLAYER_INDEX];
        }
    
        private boolean allSquaresFilledOnTheBoard() {
            int numberOfBoardSquares = BOARD_DIMENSION_SIZE * BOARD_DIMENSION_SIZE;
            int numberOfFilledBoardSquares = playersMoveCount[FIRST_PLAYER_INDEX] + playersMoveCount[SECOND_PLAYER_INDEX];
            return numberOfFilledBoardSquares == numberOfBoardSquares;
        }
    }

    /**
     * @param moves - 2-dimensional array of moves, that are done by both players(one by one).
     * 1st dimension is a move index, 2nd - board dimension index(0 - row, 1 - column) 
     * @return GameResult. Possible options are:
     * - FIRST_PLAYER 
     * - SECOND_PLAYER 
     * - DRAW 
     * - NOT_ENOUGH_MOVES
     * - IMPOSSIBLE_POSITION
     * @throws IllegalArgumentException
     */
    public GameResult getWinner(final char[][] board) {
        GameState gameState = new GameState(board);
        if (gameState.bothPlayersFilledLanes()) {
            return GameResult.IMPOSSIBLE_POSITION;
        } else if (gameState.playerWinningLaneFound[FIRST_PLAYER_INDEX]) {
            return GameResult.FIRST_PLAYER;
        } else if (gameState.playerWinningLaneFound[SECOND_PLAYER_INDEX]) {
            return GameResult.SECOND_PLAYER;
        } else if (gameState.allSquaresFilledOnTheBoard()) {
            return GameResult.DRAW; 
        }
        return GameResult.NOT_ENOUGH_MOVES;
    }
}