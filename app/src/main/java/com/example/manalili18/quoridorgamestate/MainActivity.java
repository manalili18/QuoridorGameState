package com.example.manalili18.quoridorgamestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    Button runTest;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTest = (Button)findViewById(R.id.run);
        et = (EditText)findViewById(R.id.edit);
        runTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                et.setText(""); //clear any text in edit box

                GameState firstInstance = new GameState(); //new instance of gamestate
                GameState secondInstance = new GameState(firstInstance); //use copy constructor

                firstInstance.movePawn(0, GameState.Direction.UP,false);
                et.setText(et.getText() + "player 1 moves up 1 space\n");
                firstInstance.finalizeTurn();
                et.setText(et.getText() + "player 1 finalized their turn\n");


                firstInstance.movePawn(1, GameState.Direction.DOWN,false);
                et.setText(et.getText() + "player 2 moves down 1 space\n");
                firstInstance.undo();
                et.setText(et.getText() + "player 2 undid that move\n");
                firstInstance.movePawn(1, GameState.Direction.DOWN,false);
                et.setText(et.getText() + "player 2 moves down 1 space again\n");

                firstInstance.movePawn(0, GameState.Direction.RIGHT,false);
                et.setText(et.getText() + "player 1 moves right 1 space\n");
                firstInstance.finalizeTurn();

                firstInstance.movePawn(1, GameState.Direction.LEFT,false);
                et.setText(et.getText() + "player 2 moves left 1 space\n");
                firstInstance.finalizeTurn();

                firstInstance.placeWall(0,3,4);
                et.setText(et.getText() + "player 1 places wall at intersection (3,4)\n");
                firstInstance.rotateWall(0,3,4);
                et.setText(et.getText() + "player 1 rotates wall at intersection (3,4)\n");
                firstInstance.rotateWall(0,3,4);
                et.setText(et.getText() + "player 1 rotates wall at intersection (3,4)\n");
                firstInstance.finalizeTurn();

                firstInstance.placeWall(1,4,3);
                et.setText(et.getText() + "player 2 places wall at intersection (4,3)\n");
                firstInstance.finalizeTurn();

                firstInstance.placeWall(0,5,4);
                et.setText(et.getText() + "player 1 places wall at intersection (5,4)\n");
                firstInstance.finalizeTurn();

                firstInstance.placeWall(1,4,5);
                et.setText(et.getText() + "player 2 places wall at intersection (4,5)\n");
                firstInstance.finalizeTurn();

                firstInstance.placeWall(0,4,4);
                et.setText(et.getText() + "player 1 invalid wall placement at (4,4)\n");
                firstInstance.placeWall(0,6,4);
                et.setText(et.getText() + "player 1 places wall at intersection (6,4)\n");
                firstInstance.rotateWall(0,6,4);
                et.setText(et.getText() + "player 1 invalid wall rotation at (6,4)\n");
                firstInstance.finalizeTurn();

                GameState thirdInstance = new GameState();
                GameState fourthInstance = new GameState(thirdInstance);

                et.setText(et.getText() +"\n" + secondInstance.toString());
                et.setText(et.getText() +"\n" + fourthInstance.toString());
            }
        });
    }


}
