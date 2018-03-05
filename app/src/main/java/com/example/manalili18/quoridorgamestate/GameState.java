package com.example.manalili18.quoridorgamestate;

import android.util.Log;

import static com.example.manalili18.quoridorgamestate.GameState.Direction.DOWN;
import static com.example.manalili18.quoridorgamestate.GameState.Direction.RIGHT;
import static com.example.manalili18.quoridorgamestate.GameState.Direction.UP;

import java.io.IOException;

/**
 * Created by manalili18 on 2/21/2018.
 */

public class GameState
{

    //nux told me to
    private static final long serialVersionUID = 6969420L;

    private int turn; // 0 -> player 1, 1 -> player 2
    private int[] p1Pos, p2Pos;

    private boolean[][] horzWalls;
    private boolean[][] vertWalls;

    private int p1RemainingWalls, p2RemainingWalls;

    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public GameState()
    {
        turn = 1;

        p1Pos = new int[]{4,0};
        p2Pos = new int[]{4,8};

        horzWalls = new boolean[8][8];
        vertWalls = new boolean[8][8];

        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                horzWalls[i][j] = vertWalls[i][j] = false;
            }
        }

        p1RemainingWalls = 10;
        p2RemainingWalls = 10;
    }

    //copy ctor
    public GameState(GameState g)
    {
        this.turn = g.turn;

        this.p1Pos = new int[]{g.p1Pos[0],g.p1Pos[1]};
        this.p2Pos = new int[]{g.p2Pos[0],g.p2Pos[1]};

        this.horzWalls = new boolean[8][8];
        this.vertWalls = new boolean[8][8];

        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                this.horzWalls[i][j] = g.horzWalls[i][j];
                this.vertWalls[i][j] = g.vertWalls[i][j];
            }
        }

        this.p1RemainingWalls = g.p1RemainingWalls;
        this.p2RemainingWalls = g.p2RemainingWalls;
    }

    // prints all instance variables
    // format: instance variable followed by its value(s) delimited by %%
    // instance variables delimited by newlines
    // format for 2d boolean array, rows separated by newlines, columns delimited by %%
    @Override
    public String toString()
    {
        String result = "";

        result += "turn%%" + turn + "\n";

        result += "p1Pos%%" + p1Pos[0] + "%%" + p1Pos[1] + "\n";

        result += "p2Pos%%" + p2Pos[0] + "%%" + p2Pos[1] + "\n";

        result += "horzWalls\n";

        result += wallMatrixToString(horzWalls);
        result += wallMatrixToString(vertWalls);

        result += "p1RemainingWalls%%" + p1RemainingWalls + "\n";
        result += "p2RemainingWalls%%" + p2RemainingWalls + "\n";

        return result;
    }


    /**
     * print each boolean in horzWalls
     * elements delimited by %%
     * rows delimited by newlines
     *
     * TODO: should this be static?
     */
    private String wallMatrixToString( boolean[][] wallMatrix)
    {

        String result = "";

        for( boolean[] row : wallMatrix )
        {
            int i = 0;
            for( boolean b : row )
            {
                result += b;

                //TODO: formal citation - https://stackoverflow.com/questions/41591107/detect-last-foreach-loop-iteration
                if(i++ != row.length - 1) result += "%%";
            }
            result += "\n";
        }

        return result;
    }

    //TODO: movePawn method
    public boolean movePawn(int player, Direction dir, boolean jump)
    {
        //moving player is in first slot of bothPlayers[]
        //int[][] bothPlayers = (player == 0) ? new int[][]{p1Pos,p2Pos} : new int[][]{p2Pos,p1Pos};
        int[][] bothPlayers = new int[][] {p1Pos, p2Pos};
        //check bounds
        if(player  != 0 || player != 1)
        {
            return false;
        }
        //make sure the player can move
        if(player != turn)
        {
            return false;
        }
        //check if valid move
        //ie, check for walls, other players
        switch(dir){
            case UP:
                moveUp(bothPlayers[player], bothPlayers[1-player],jump);
                break;
            case DOWN:
                moveDown(bothPlayers[player], bothPlayers[1-player],jump);
                break;

            case RIGHT:
                moveRight(bothPlayers[player], bothPlayers[1-player], jump);
                break;
            case LEFT:
                moveLeft(bothPlayers[player],bothPlayers[1-player],jump);
                break;
            default:
                Log.i("movePawn","Something went wrong");
        }
        return false;
    }

    /**
     * Method to move pawn up
     * @param currentPlayer
     * @param otherPlayer
     * @param jump true move left, false move right
     * @return
     */
    public boolean moveUp(int[] currentPlayer, int[] otherPlayer, boolean jump)
    {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try
        {
            if(curY == 0) //player is trying to move past top
            {
                return false;
            }
            //check if players are adjacent
            if(otherX == curX && otherY-1 == curY)
            {
                if(horzWalls[curX-1][curY-2] || horzWalls[curX][curY-2])
                {
                    if(jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls on left side
                        if(vertWalls[curX-1][curY-1] || vertWalls[curX-1][curY-2])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]--;
                            currentPlayer[1]--;
                            return true;
                        }
                    }
                    else
                    {
                        //check if there are no blocking walls on right side
                        if(vertWalls[curX][curY-1] || vertWalls[curX][curY-2])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]++;
                            currentPlayer[1]--;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else
                {
                    currentPlayer[1] -= 2; //jump over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls in front
            else if(horzWalls[curX-1][curY-1] || horzWalls[curX][curY-1])
            {
                return false;
            }
            else
            {
                currentPlayer[1]--; //move player up one space
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException ai)
        {
            return false;
        }

        return false;
    }

    /**
     * move pawn down one space
     * @param currentPlayer
     * @param otherPlayer
     * @param jump true, move left, false move right
     * @return
     */
    public boolean moveDown(int[] currentPlayer, int[] otherPlayer, boolean jump)
    {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try
        {
            if(curY == 8) //player is trying to move past bot
            {
                return false;
            }
            //check if players are adjacent
            if(otherX == curX && otherY+1 == curY)
            {
                //check if far walls exist
                if(horzWalls[curX-1][curY+1] || horzWalls[curX][curY+1])
                {
                    if(jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls on left side
                        if(vertWalls[curX-1][curY] || vertWalls[curX-1][curY+1])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]--;
                            currentPlayer[1]++;
                            return true;
                        }
                    }
                    else
                    {
                        //check if there are no blocking walls on right side
                        if(vertWalls[curX][curY] || vertWalls[curX][curY+1])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]++;
                            currentPlayer[1]++;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else
                {
                    currentPlayer[1] += 2; //jump over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls in front
            else if(horzWalls[curX-1][curY] || horzWalls[curX][curY])
            {
                return false;
            }
            else
            {
                currentPlayer[1]++; //move player up one space
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException ai)
        {
            return false;
        }

        return false;
    }

    /**
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump
     * @return
     */
    public boolean moveLeft(int[] currentPlayer, int[] otherPlayer, boolean jump)
    {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try
        {
            if(curX == 0) //player is trying to move left side off board
            {
                return false;
            }
            //check if players are adjacent
            if(otherX-1 == curX && otherY == curY)
            {
                //check if far walls exist
                if(vertWalls[curX-2][curY-1] || vertWalls[curX-2][curY])
                {
                    if(jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls above
                        if(horzWalls[curX-2][curY-1] || horzWalls[curX-1][curY-1])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]--;
                            currentPlayer[1]--;
                            return true;
                        }
                    }
                    else
                    {
                        //check if there are no blocking walls below
                        if(horzWalls[curX-2][curY] || horzWalls[curX-1][curY])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]--;
                            currentPlayer[1]++;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else
                {
                    currentPlayer[0] -= 2; //jump left over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls on left side
            else if(vertWalls[curX-1][curY] || horzWalls[curX-1][curY-1])
            {
                return false;
            }
            else
            {
                currentPlayer[0]--; //move player left one space
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException ai)
        {
            return false;
        }

        return false;
    }

    /**
     *
     * @param currentPlayer
     * @param otherPlayer
     * @param jump
     * @return
     */
    public boolean moveRight(int[] currentPlayer, int[] otherPlayer, boolean jump)
    {
        int curX = currentPlayer[0];
        int curY = currentPlayer[1];
        int otherX = otherPlayer[0];
        int otherY = otherPlayer[1];
        try
        {
            if(curX == 0) //player is trying to move left side off board
            {
                return false;
            }
            //check if players are adjacent
            if(otherX+1 == curX && otherY == curY)
            {
                //check if far walls exist
                if(vertWalls[curX+1][curY-1] || vertWalls[curX+1][curY])
                {
                    if(jump) //jump diagonally to the left
                    {
                        //check if there are no blocking walls above
                        if(horzWalls[curX][curY-1] || horzWalls[curX+1][curY-1])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]++;
                            currentPlayer[1]--;
                            return true;
                        }
                    }
                    else
                    {
                        //check if there are no blocking walls below
                        if(horzWalls[curX][curY] || horzWalls[curX+1][curY])
                        {
                            return false;
                        }
                        else
                        {
                            currentPlayer[0]++;
                            currentPlayer[1]++;
                            return true;
                        }
                    } //elif for jump case

                } //if for far walls
                else
                {
                    currentPlayer[0] += 2; //jump left over the adjacent player
                }
            }//if for player adjacency
            //check if there are walls on right side
            else if(vertWalls[curX][curY-1] || horzWalls[curX][curY])
            {
                return false;
            }
            else
            {
                currentPlayer[0]++; //move player left one space
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException ai)
        {
            return false;
        }

        return false;
    }

    public boolean finalizeTurn()
    {
        turn = 1-turn;
        return true;
    }

    //TODO: placeWall method
    //placeWall
    //Checks for which player is placing the wall
    //Returns false if player does not have any available walls
    //Checks if placeWall is a valid move
    //TODO: Figure out how to deal with finalize turn and closed path
    public boolean placeWall(int player, int x, int y)
    {
        try {
            //check bounds
            if (player == 1) // player 1
            {
                if (p1RemainingWalls == 0)
                    return false;
                    //check if spot is available (horz and vert)
                else if (horzWalls[x][y] == false && vertWalls[x][y] == false) // default to horzWall place first
                {
                    if ((horzWalls[x - 1][y] == true || horzWalls[x + 1][y] == true) && (vertWalls[x][y - 1] == false || vertWalls[x][y + 1] == false)) {
                        vertWalls[x][y] = true;
                        p1RemainingWalls--;
                        return true;
                    } else if ((vertWalls[x][y - 1] == true || vertWalls[x][y + 1] == true) && (horzWalls[x - 1][y] == false || horzWalls[x + 1][y] == false)) {
                        horzWalls[x][y] = true;
                        p1RemainingWalls--;
                        return true;
                    }
                    // Default Case
                    else if ((horzWalls[x - 1][y] == false && horzWalls[x + 1][y] == false) && (vertWalls[x][y - 1] == false && vertWalls[x][y + 1] == false)) {
                        horzWalls[x][y] = true;
                        p1RemainingWalls--;
                        return true;
                    } else
                        return false;
                } else
                    return false;
            } else if (player == 2) // player 2
            {
                if (p2RemainingWalls == 0)
                    return false;
                else if (horzWalls[x][y] == false && vertWalls[x][y] == false) // default to horzWall place first
                {
                    // Checks for overlapping walls
                    if ((horzWalls[x - 1][y] == true || horzWalls[x + 1][y] == true) && (vertWalls[x][y - 1] == false || vertWalls[x][y + 1] == false)) {
                        vertWalls[x][y] = true;
                        p2RemainingWalls--;
                        return true;
                    }
                    // Checks for overlapping walls
                    else if ((vertWalls[x][y - 1] == true || vertWalls[x][y + 1] == true) && (horzWalls[x - 1][y] == false || horzWalls[x + 1][y] == false)) {
                        horzWalls[x][y] = true;
                        p2RemainingWalls--;
                        return true;
                    }
                    // Default Case
                    else if ((horzWalls[x - 1][y] == false && horzWalls[x + 1][y] == false) && (vertWalls[x][y - 1] == false && vertWalls[x][y + 1] == false)) {
                        horzWalls[x][y] = true;
                        p2RemainingWalls--;
                        return true;
                    } else
                        return false;
                } else
                    return false;
            } else
                return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    //TODO: rotateWall method
    //TODO: how are we going to identify newly placed walls? does the framework handle this?
    public boolean rotateWall(int player, int x, int y) {
        try {
            if (player == 1) // player 1
            {
                if (horzWalls[x][y] == true) {
                    if (vertWalls[x][y - 1] == true || vertWalls[x][y + 1] == true) {
                        return false;
                    } else {
                        vertWalls[x][y] = true;
                        horzWalls[x][y] = false;
                        return true;
                    }
                } else if (vertWalls[x][y] == true) {
                    if (horzWalls[x - 1][y] == true || horzWalls[x + 1][y] == true) {
                        return false;
                    } else {
                        horzWalls[x][y] = true;
                        vertWalls[x][y] = false;
                        return true;
                    }
                } else
                    return false;
            } else if (player == 2) {
                if (horzWalls[x][y] == true) {
                    if (vertWalls[x][y - 1] == true || vertWalls[x][y + 1] == true) {
                        return false;
                    } else {
                        vertWalls[x][y] = true;
                        horzWalls[x][y] = false;
                        return true;
                    }
                } else if (vertWalls[x][y] == true) {
                    if (horzWalls[x - 1][y] == true || horzWalls[x + 1][y] == true) {
                        return false;
                    } else {
                        horzWalls[x][y] = true;
                        vertWalls[x][y] = false;
                        return true;
                    }
                } else
                    return false;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

}
