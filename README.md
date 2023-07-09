# Tictactoe game
## Questions
1. What are the possible game results?
For example, first player winner, second player winner, draw(?), not enough moves done, impossible game.
2. Should the javadocs be provided for the following solution?
3. Are there any requirements for the build system and Junit version, which should be used(Maven, Gradle, Junit 4/5)?

### In case game is provided as a list of moves
1. API definition - should we pass all moves as one array, or execute makeMove() function for each move? What type of the result is expected - string, int, enum, char etc.?
2. Should we throw IllegalArgumentException in case if:
- user provides the move outside of the board
- user tries to fill non-empty square
- user provides empty x and y coordinates([])
3. In case if array is an input, and first player won after k moves, but array contains more than k values, should we just return, that first player won?
### In case game is provided as a board
1. Should we throw IllegalArgumentException in case if:
- first player makes less moves than the second one
- user tries to fill non-empty square with random symbol
- user does not provide expected row/column, i.e. provided rows number is 2, when expected board size equals 3