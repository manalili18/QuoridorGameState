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
                firstInstance.finalizeTurn();
                et.setText(et.getText() + "player 1 moves up 1 space\n");

                firstInstance.movePawn(1, GameState.Direction.DOWN,false);
                firstInstance.finalizeTurn();
                et.setText(et.getText() + "player 2 moves down 1 space\n");


                firstInstance.movePawn(0, GameState.Direction.RIGHT,false);
                et.setText(et.getText() + "player 1 moves right 1 space\n");
                firstInstance.finalizeTurn();

                firstInstance.movePawn(1, GameState.Direction.LEFT,false);
                et.setText(et.getText() + "player 2 moves left 1 space\n");
                firstInstance.finalizeTurn();

                GameState thirdInstance = new GameState();
                GameState fourthInstance = new GameState(thirdInstance);

                et.setText(et.getText() +"\n" + secondInstance.toString());
                et.setText(et.getText() +"\n" + fourthInstance.toString());
            }
        });
    }


}
