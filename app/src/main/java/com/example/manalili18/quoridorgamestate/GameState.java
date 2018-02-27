package com.example.manalili18.quoridorgamestate;

import android.util.Log;

import java.io.IOException;

/**
 * Created by manalili18 on 2/21/2018.
 */

public class GameState
{

    //nux told me to
    private static final long serialVersionUID = 696969696969420L;

    private int turn; // 1 -> player 1, 2 -> player 2
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
    public boolean movePawn(int player, Direction dir)
    {
        //check bounds

        //check if valid move
        //ie, check for walls, other players
        switch(dir){
            case UP:
                break;
            case RIGHT:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            default:
                Log.i("movePawn","Something went wrong");
        }
        return false;
    }

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
