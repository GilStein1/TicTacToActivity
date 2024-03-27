package com.example.tictactoactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton[][] grid;
    ImageView ivWhichTurn;
    TextView tvMessage;
    Button btnBack;
    Controller controller;
    int wins;
    int losses;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initialize();

        Intent intent = getIntent();
        wins = Integer.valueOf(intent.getExtras().getString("wins"));
        losses = Integer.valueOf(intent.getExtras().getString("losses"));
        boolean easyDifficulty = intent.getBooleanExtra("difficulty-easy",true);

        controller = new Controller(easyDifficulty,grid,ivWhichTurn,btnBack,tvMessage);
    }
    private void initialize() {
        grid = new ImageButton[3][3];
        grid[0][0] = findViewById(R.id.btn00);
        grid[0][1] = findViewById(R.id.btn01);
        grid[0][2] = findViewById(R.id.btn02);
        grid[1][0] = findViewById(R.id.btn10);
        grid[1][1] = findViewById(R.id.btn11);
        grid[1][2] = findViewById(R.id.btn12);
        grid[2][0] = findViewById(R.id.btn20);
        grid[2][1] = findViewById(R.id.btn21);
        grid[2][2] = findViewById(R.id.btn22);

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j].setOnClickListener(this);
            }
        }

        ivWhichTurn = findViewById(R.id.ivWhichTurn);
        tvMessage = findViewById(R.id.tvMessage);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(v == grid[i][j]) {
                    controller.buttonPressed(i,j);
                }
            }
        }
        if(v == btnBack) {
            if(controller.playerWon()) {
                wins++;
            }
            else if(controller.computerWon()) {
                losses++;
            }
            Intent intent = getIntent();
            intent.putExtra("wins2", String.valueOf(wins));
            intent.putExtra("losses2", String.valueOf(losses));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}