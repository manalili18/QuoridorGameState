package com.example.manalili18.quoridorgamestate;

import android.util.Log;

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

    //TODO: placeWall method
    public boolean placeWall(int player, int x, int y)
    {
        //check bounds

        //check if spot is available (horz and vert)
        //check if spot is horizontal compatible
            //place horz wall
        //else check if spot is vertical compatible
            //place vert wall

        return false;
    }

    //TODO: rotateWall method
    //TODO: how are we going to identify newly placed walls? does the framework handle this?
    public boolean rotateWall(int player, int x, int y)
    {
        //check bounds
        //check if wall exists (vert/horz)
        //check if rotated wall is valid
            //flip bool in both matrices

        return false;
    }


}
