package com.example.manalili18.quoridorgamestate;

/**
 * Created by manalili18 on 2/21/2018.
 */

public class GameState
{

    private int turn; // 1 -> player 1, 2 -> player 2
    private int[] p1Pos, p2Pos;

    private boolean[][] horzWalls;
    private boolean[][] vertWalls;

    private int p1RemainingWalls, p2RemainingWalls;

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


}
