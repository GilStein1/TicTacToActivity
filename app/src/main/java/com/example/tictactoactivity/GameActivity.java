package com.example.tictactoactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton[][] grid;
    LinearLayout[] layouts;
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
        layouts = new LinearLayout[3];
        LinearLayout lMainLayout = findViewById(R.id.lMainLayout);
        for(int i = 0; i < grid.length; i++) {
            layouts[i] = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layouts[i].setLayoutParams(layoutParams);
            layouts[i].setOrientation(LinearLayout.HORIZONTAL);
            lMainLayout.addView(layouts[i]);
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new ImageButton(this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(100,100);
                grid[i][j].setLayoutParams(buttonParams);
                grid[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                grid[i][j].setOnClickListener(this);
                layouts[i].addView(grid[i][j]);
            }
        }
        tvMessage = new TextView(this);
        btnBack = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvMessage.setTextSize(30);
        tvMessage.setTextColor(Color.WHITE);
        tvMessage.setLayoutParams(params);
        lMainLayout.addView(tvMessage);

        btnBack.setText("back to main page");
        btnBack.setVisibility(View.GONE);
        btnBack.setLayoutParams(params);
        lMainLayout.addView(btnBack);

        ivWhichTurn = findViewById(R.id.ivWhichTurn);
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