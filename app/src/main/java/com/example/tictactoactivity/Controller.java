package com.example.tictactoactivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Controller {
    private Model model;
    private ImageButton[][] imgArr;
    private ImageView turnSign;
    private Button exitButton;
    private TextView tvMessage;
    private boolean playerTurn;
    public Controller(boolean difficulty, ImageButton[][] imgArr,
                      ImageView turnSign,
                      Button exitButton,
                      TextView tvMessage) {
        playerTurn = true;
        model = new Model(difficulty);
        this.imgArr = imgArr;
        this.turnSign = turnSign;
        this.exitButton = exitButton;
        this.tvMessage = tvMessage;
    }
    public void buttonPressed(int column, int row) {
        if(playerTurn && !isGameOver()) {
            if(setShape(column,row,1)) {
                imgArr[column][row].setImageResource(R.drawable.xshape);
                playerTurn = false;
                if(!isGameOver()) {
                    turnSign.setImageResource(R.drawable.oshape);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        model.playTurn();
                        imgArr[model.getLastComputerTurnColumn()][model.getLastComputerTurnRow()].setImageResource(R.drawable.oshape);
                        handler.postDelayed(() -> {
                            playerTurn = true;
                            turnSign.setImageResource(R.drawable.xshape);
                            if(isGameOver()) {
                                endGame();
                            }
                        },500);
                    }, 1000);
                }
                if(isGameOver()) {
                    endGame();
                }
            }
        }
    }
    private void endGame() {
        exitButton.setVisibility(View.VISIBLE);
        if(model.getWinner() == 0) {
            tvMessage.setText("Its A Tie!");
        }
        else if(model.getWinner() == 1) {
            tvMessage.setText("You Won!");
        }
        else if(model.getWinner() == 2) {
            tvMessage.setText("You Lost!");
        }
    }
    public boolean isEmpty(int column, int row) {
        return model.isEmpty(column,row,model.getGrid());
    }
    public boolean isGameOver() {
        return model.isGameOver();
    }
    public boolean setShape(int column, int row, int value) {
        return model.setShape(column,row,value);
    }
    public boolean playerWon() {
        return model.getWinner() == 1;
    }
    public boolean computerWon() {
        return model.getWinner() == 2;
    }
}
