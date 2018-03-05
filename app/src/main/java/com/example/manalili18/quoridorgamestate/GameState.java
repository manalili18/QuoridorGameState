package com.example.manalili18.quoridorgamestate;

import android.util.Log;

import static com.example.manalili18.quoridorgamestate.GameState.Direction.DOWN;
import static com.example.manalili18.quoridorgamestate.GameState.Direction.RIGHT;
import static com.example.manalili18.quoridorgamestate.GameState.Direction.UP;

import java.io.IOException;

/**
 * Created by manalili18 on 2/21/2018.
 */

public class GameState {

    //nux told me to
    private static final long serialVersionUID = 6969420L;

    private int turn; // 0 -> player 1, 1 -> player 2
    private int[] p1Pos, p2Pos, tempPos;

    private boolean[][] horzWalls, tempHWalls;
    private boolean[][] vertWalls, tempVWalls;

    private int p1RemainingWalls,tempRemWalls, p2RemainingWalls;


    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public GameState() {
        turn = 0;
        p1Pos = new int[]{4, 0};
        p2Pos = new int[]{4, 8};
        tempPos = new int[]{0,0};
        tempPos[0] = p1Pos [0];
        tempPos[1] = p1Pos [1];
        horzWalls = new boolean[8][8];
        vertWalls = new boolean[8][8];
        tempVWalls = new boolean[8][8];
        tempHWalls = new boolean[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempVWalls[i][j] = tempHWalls[i][j] = horzWalls[i][j] = vertWalls[i][j] = false;
            }
        }

        tempRemWalls = p1RemainingWalls = p2RemainingWalls = 10;


    }

    //copy ctor
    public GameState(GameState g) {
        this.turn = g.turn;

        this.p1Pos = new int[]{g.p1Pos[0], g.p1Pos[1]};
        this.p2Pos = new int[]{g.p2Pos[0], g.p2Pos[1]};

        this.tempPos = new int[]{0,0};
        this.tempPos[0] = (this.turn == 0) ? p1Pos[0] : p2Pos[0];
        this.tempPos[1] = (this.turn == 1) ? p1Pos[1] : p2Pos[1];

        this.horzWalls = new boolean[8][8];
        this.vertWalls = new boolean[8][8];
        this.tempVWalls = new boolean[8][8];
        this.tempHWalls = new boolean[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.tempHWalls[i][j] = this.horzWalls[i][j] = g.horzWalls[i][j];
                this.tempVWalls[i][j] = this.vertWalls[i][j] = g.vertWalls[i][j];
            }
        }

        this.p1RemainingWalls = g.p1RemainingWalls;
        this.p2RemainingWalls = g.p2RemainingWalls;

        this.tempRemWalls = (this.turn == 1) ? p1RemainingWalls : p2RemainingWalls;
    }

    // prints all instance variables
    // format: instance variable followed by its value(s) delimited by %%
    // instance variables delimited by newlines
    // format for 2d boolean array, rows separated by newlines, columns delimited by %%
    @Override
    public String toString() {
        String result = "";

        result += "turn%%" + turn + "\n";

        result += "p1Pos%%" + p1Pos[0] + "%%" + p1Pos[1] + "\n";

        result += "p2Pos%%" + p2Pos[0] + "%%" + p2Pos[1] + "\n";

        result += "horzWalls\n";

        result += wallMatrixToString(horzWalls);
        result += "Vert Walls\n";
        result += wallMatrixToString(vertWalls);

        result += "p1RemainingWalls%%" + p1RemainingWalls + "\n";
        result += "p2RemainingWalls%%" + p2RemainingWalls + "\n";

        return result;
    }

    /**
     * toString helper method for 2d boolean matrices.
     * print each boolean in horzWalls
     * elements delimited by %%
     * rows delimited by newlines
     *
     * @param wallMatrix
     * @return String representation of input matrix
     */
    private String wallMatrixToString(boolean[][] wallMatrix) {

        String result = "";

        for (boolean[] row : wallMatrix) {
            int i = 0;
            for (boolean b : row) {
                result += b;

                //TODO: formal citation - https://stackoverflow.com/questions/41591107/detect-last-foreach-loop-iteration
                if (i++ != row.length - 1) result += "%%";
            }
            result += "\n";
        }

        return result;
    }

    /**
     * Move pawn according to direction. If necessary, referrences jump for various jump
     * cases.
     *
     * @param player who's turn it is
     * @param dir direction of movement
     * @param jump boolean that determines extra diagonal jumps
     * @return true if success, else false
     */
    public boolean movePawn(int player, Direction dir, boolean jump) {
        //moving player is in first slot of bothPlayers[]
        int[][] bothPlayers = new int[][]{p1Pos, p2Pos};
        //check bounds
        if (player < 0 || player > 1) {
            return false;
        }
        //make sure the player can move
        if (player != turn) {
            return false;
        }
        //check if valid move
        //ie, check for walls, other players
        switch (dir) {
            case UP:
                moveUp(bothPlayers[player], bothPlayers[1 - player], jump);
                Log.i("movePawn", "moved player up");
                break;
            case DOWN:
                moveDown(bothPlayers[player], bothPlayers[1 - player], jump);
                Log.i("movePawn", "moved down");
                break;
            case RIGHT:
                moveRight(bothPlayers[player], bothPlayers[1 - player], jump);
                break;
            case LEFT:
                moveLeft(bothPlayers[player], bothPlayers[1 - player], jump);
                break;
            default:
                Log.i("movePawn", "Something went wrong");
        }
        return false;
    }

    /**
     * Method to move pawn up
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump          true move left, false move right
     * @return
     */
    public boolean moveUp(int[] currentPlayer, int[] otherPlayer, boolean jump) {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try {
            if (curY == 0) //player is trying to move past top
            {
                return false;
            }
            //check if players are adjacent
            if (otherX == curX && otherY - 1 == curY) {
                if (horzWalls[curX - 1][curY - 2] || horzWalls[curX][curY - 2]) {
                    if (jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls on left side
                        if (vertWalls[curX - 1][curY - 1] || vertWalls[curX - 1][curY - 2]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]-1;
                            tempPos[1] = currentPlayer[1]-1;
                            return true;
                        }
                    } else {
                        //check if there are no blocking walls on right side
                        if (vertWalls[curX][curY - 1] || vertWalls[curX][curY - 2]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]+1;
                            tempPos[1] = currentPlayer[1]-1;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else {
                    tempPos[0] = currentPlayer[0];
                    tempPos[1] = currentPlayer[1] - 2; //jump over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls in front
            else if (horzWalls[curX - 1][curY - 1] || horzWalls[curX][curY - 1]) {
                return false;
            } else {
                tempPos[0] = curX;
                tempPos[1] = currentPlayer[1]-1; //move player up one space
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException ai) {
            return false;
        }

        return false;
    }

    /**
     * move pawn down one space
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump          true, move left, false move right
     * @return
     */
    public boolean moveDown(int[] currentPlayer, int[] otherPlayer, boolean jump) {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try {
            if (curY == 8) //player is trying to move past bot
            {
                return false;
            }
            //check if players are adjacent
            if (otherX == curX && otherY + 1 == curY) {
                //check if far walls exist
                if (horzWalls[curX - 1][curY + 1] || horzWalls[curX][curY + 1]) {
                    if (jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls on left side
                        if (vertWalls[curX - 1][curY] || vertWalls[curX - 1][curY + 1]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]-1;
                            tempPos[1] = currentPlayer[1]+1;
                            return true;
                        }
                    } else {
                        //check if there are no blocking walls on right side
                        if (vertWalls[curX][curY] || vertWalls[curX][curY + 1]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]+1;
                            tempPos[1] = currentPlayer[1]+1;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else {
                    currentPlayer[1] += 2; //jump over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls in front
            else if (horzWalls[curX - 1][curY] || horzWalls[curX][curY]) {
                return false;
            } else {
                tempPos[0] = currentPlayer[0];
                tempPos[1] = currentPlayer[1]+1; //move player up one space
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException ai) {
            return false;
        }

        return false;
    }

    /**
     * method to move the players
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump
     * @return
     */
    public boolean moveLeft(int[] currentPlayer, int[] otherPlayer, boolean jump) {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try {
            if (curX == 0) //player is trying to move left side off board
            {
                return false;
            }
            //check if players are adjacent
            if (otherX - 1 == curX && otherY == curY) {
                //check if far walls exist
                if (vertWalls[curX - 2][curY - 1] || vertWalls[curX - 2][curY]) {
                    if (jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls above
                        if (horzWalls[curX - 2][curY - 1] || horzWalls[curX - 1][curY - 1]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]-1;
                            tempPos[1] = currentPlayer[1]-1;
                            return true;
                        }
                    } else {
                        //check if there are no blocking walls below
                        if (horzWalls[curX - 2][curY] || horzWalls[curX - 1][curY]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]-1;
                            tempPos[0] = currentPlayer[1]+1;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else {
                    tempPos[0] = currentPlayer[0] - 2; //jump left over the adjacent player
                    tempPos[1] = currentPlayer[1];
                }
            }//if for player adjacency
            //check if there are walls on left side
            else if (vertWalls[curX - 1][curY] || horzWalls[curX - 1][curY - 1]) {
                return false;
            } else {
                tempPos[0] = currentPlayer[0]-1; //move player left one space
                tempPos[1] = currentPlayer[1];
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException ai) {
            return false;
        }

        return false;
    }

    /**
     * Method to move the current player one space to the right
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump          move up if true, move down if false
     * @return
     */
    public boolean moveRight(int[] currentPlayer, int[] otherPlayer, boolean jump) {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try {
            if (curX == 0) //player is trying to move left side off board
            {
                return false;
            }
            //check if players are adjacent
            if (otherX + 1 == curX && otherY == curY) {
                //check if far walls exist
                if (vertWalls[curX + 1][curY - 1] || vertWalls[curX + 1][curY]) {
                    if (jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls above
                        if (horzWalls[curX][curY - 1] || horzWalls[curX + 1][curY - 1]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]+1;
                            tempPos[1] = currentPlayer[1]-1;
                            return true;
                        }
                    } else {
                        //check if there are no blocking walls below
                        if (horzWalls[curX][curY] || horzWalls[curX + 1][curY]) {
                            return false;
                        } else {
                            tempPos[0] = currentPlayer[0]+1;
                            tempPos[1] = currentPlayer[1]+1;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else {
                    tempPos[0] = currentPlayer[0] + 2; //jump left over the adjacent player
                    tempPos[1] = currentPlayer[1];
                }
            }//if for player adjacency
            //check if there are walls on right side
            else if (vertWalls[curX][curY - 1] || horzWalls[curX][curY]) {
                return false;
            } else {
                tempPos[0] = currentPlayer[0]+1; //move player left one space
                tempPos[1] = currentPlayer[1];
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException ai) {
            return false;
        }

        return false;
    }

    /**
     * Finalize temp values to reflect actual board changes.
     * @return true. Always.
     */
    public boolean finalizeTurn() {
        //check who's turn it is and update their values
        if(turn == 0){
            p1Pos[0] = tempPos[0];
            p1Pos[1] = tempPos[1];
            p1RemainingWalls = tempRemWalls;
        } else {
            p2Pos[0] = tempPos[0];
            p2Pos[1] = tempPos[1];
            p2RemainingWalls = tempRemWalls;
        }

        //update walls on board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                horzWalls[i][j] = tempHWalls[i][j];
                vertWalls[i][j] = tempVWalls[i][j];
            }
        }

        turn = 1 - turn;
        //set tempPos to prevent accidentally setting position on wall placement
        if(turn == 0){
            tempPos[0] = p1Pos[0];
            tempPos[1] = p1Pos[1] ;
            tempRemWalls = p1RemainingWalls;
        } else {
            tempPos[0] = p2Pos[0];
            tempPos[1] = p2Pos[1] ;
            tempRemWalls = p2RemainingWalls;
        }
        return true;
    }

    /**
     * Resets the board to the beginning of the turn.
     * @return true?
     */
    public boolean undo(){
        //check who's turn it is and reset their values
        if(turn == 0){
            tempPos[0] = p1Pos[0];
            tempPos[1] = p1Pos[1] ;
            tempRemWalls = p1RemainingWalls;
        } else {
            tempPos[0] = p2Pos[0];
            tempPos[1] = p2Pos[1] ;
            tempRemWalls = p2RemainingWalls;
        }

        //update walls on board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempHWalls[i][j] = horzWalls[i][j];
                tempVWalls[i][j] = vertWalls[i][j];
            }
        }
        return true;
    }

    //TODO: placeWall method
    //placeWall
    //Checks for which player is placing the wall
    //Returns false if player does not have any available walls
    //Checks if placeWall is a valid move
    //TODO: Figure out how to deal with finalize turn and closed path
    public boolean placeWall(int player, int x, int y) {
        //checks for player turn, returns false if not turn
        if (player != turn)
            return false;
        //check bounds
        if (borderPlaceCheck(player, x, y)) {
            if (player == 0)
                p1RemainingWalls = tempRemWalls;
            else
                p2RemainingWalls = tempRemWalls;
            return true;
        } else
            return false;
    }


    private boolean borderPlaceCheck(int player, int x, int y) {
        if (turn == 0) // player 1
            tempRemWalls = p1RemainingWalls;
        else
            tempRemWalls = p2RemainingWalls;
        if (tempRemWalls == 0)
            return false;
        if (!tempHWalls[x][y] && !tempVWalls[x][y]) // default to horzWall place first
        {
            if (x == 0 && y == 0) {
                if (tempHWalls[x + 1][y] && !tempVWalls[x][y + 1]) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y + 1] && !tempHWalls[x + 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x + 1][y] && !tempVWalls[x][y + 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (x == 0 && y == 8) {
                if ((tempHWalls[x + 1][y]) && (!tempVWalls[x][y - 1])) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y - 1] && !tempHWalls[x + 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x + 1][y] && !tempVWalls[x][y - 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (x == 8 && y == 0) {
                if (tempHWalls[x - 1][y] && !tempVWalls[x][y + 1]) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y + 1] && !tempHWalls[x - 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x - 1][y] && !tempVWalls[x][y + 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (x == 8 && y == 8) {
                if (tempHWalls[x - 1][y] && !tempVWalls[x][y - 1]) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y - 1] && !tempHWalls[x - 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x - 1][y] && !tempVWalls[x][y - 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (x == 0) {
                if (tempHWalls[x + 1][y] && (!tempVWalls[x][y - 1] || !tempVWalls[x][y + 1])) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if ((tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) && !tempHWalls[x + 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x + 1][y] && (!tempVWalls[x][y - 1] && !tempVWalls[x][y + 1])) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (x == 8) {
                if (tempHWalls[x - 1][y] && (!tempVWalls[x][y - 1] || !tempVWalls[x][y + 1])) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if ((tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) && !tempHWalls[x - 1][y]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if (!tempHWalls[x - 1][y] && (!tempVWalls[x][y - 1] && !tempVWalls[x][y + 1])) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (y == 0) {
                if ((tempHWalls[x - 1][y] || tempHWalls[x + 1][y]) && !tempVWalls[x][y + 1]) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y + 1] && (!tempHWalls[x - 1][y] || !tempHWalls[x + 1][y])) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if ((!tempHWalls[x - 1][y] && !tempHWalls[x + 1][y]) && !tempVWalls[x][y + 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else if (y == 8) {
                if (((tempHWalls[x - 1][y]) || (tempHWalls[x + 1][y])) && (!tempVWalls[x][y - 1])) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if (tempVWalls[x][y - 1] && ((!tempHWalls[x - 1][y]) || (!tempHWalls[x + 1][y]))) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if ((!tempHWalls[x - 1][y] && !tempHWalls[x + 1][y]) && !tempVWalls[x][y - 1]) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            } else {
                //check if spot is available (horz and vert)
                if ((tempHWalls[x - 1][y] || tempHWalls[x + 1][y]) && (!tempVWalls[x][y - 1] || !tempVWalls[x][y + 1])) {
                    tempVWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else if ((tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) && (!tempHWalls[x - 1][y] || !tempHWalls[x + 1][y])) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                }
                // Default Case
                else if ((!tempHWalls[x - 1][y] && !tempHWalls[x + 1][y]) && (!tempVWalls[x][y - 1] && !tempVWalls[x][y + 1])) {
                    tempHWalls[x][y] = true;
                    tempRemWalls--;
                    return true;
                } else
                    return false;
            }
        } else
            return false;
    }

    //TODO: rotateWall method
    //TODO: how are we going to identify newly placed walls? does the framework handle this?
    public boolean rotateWall(int player, int x, int y) {
        //checks for player turn, returns false if not turn
        if (player != turn)
            return false;
        return borderRotateCheck(x, y);
    }

    private boolean borderRotateCheck(int x, int y) {
        if (x == 0 && y == 0) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (x == 0 && y == 8) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (x == 8 && y == 0) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (x == 8 && y == 8) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (x == 0) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (x == 8) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (y == 0) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y] || tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else if (y == 8) {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y] || tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        } else {
            if (tempHWalls[x][y]) {
                if (tempVWalls[x][y - 1] || tempVWalls[x][y + 1]) {
                    return false;
                } else {
                    tempVWalls[x][y] = true;
                    tempHWalls[x][y] = false;
                    return true;
                }
            } else if (tempVWalls[x][y]) {
                if (tempHWalls[x - 1][y] || tempHWalls[x + 1][y]) {
                    return false;
                } else {
                    tempHWalls[x][y] = true;
                    tempVWalls[x][y] = false;
                    return true;
                }
            } else
                return false;
        }
    }





}
