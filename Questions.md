1. Is the board finite? Does it have constant dimension size(3, for example)?
2. Is columns count always equals rows count?
3. Is the line always equal to the board size?
4. Should we make line size as a parameter?(for example, 3 in a row, 4 in a row etc.) 
5. API definition - should we pass all moves as one array, or execute makeMove() function for each move? What type of the result is expected - string, int, enum, char etc.?
6. What are the possible game results?
For example, first player winner, second player winner, draw(?), not enough moves done, impossible game.
7. Should we throw IllegalArgumentException in case if:
- user provides the move outside of the board
- user tries to fill non-empty square
- user provides empty x and y coordinates([])
8. Should the javadocs be provided for the following solution?
9. Are there any requirements for the build system and Junit version, which should be used(Maven, Gradle, Junit 4/5)?
10. In case if array is an input, and first player won after k moves, but array contains more than k values, should we just return, that first player won?