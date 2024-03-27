package com.example.tictactoactivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int wins;
    int losses;
    Button btnStart;
    TextView tvWins, tvLosses;
    ActivityResultLauncher<Intent> activityLauncher;
    RadioButton rbEasy, rbHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeValues();
        initViews();
    }
    private void initViews() {
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        tvWins = findViewById(R.id.tvWins);
        tvLosses = findViewById(R.id.tvLosses);
        rbHard = findViewById(R.id.rbHard);
        rbEasy = findViewById(R.id.rbEasy);
        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(this,"game was stopped unexpectedly",Toast.LENGTH_SHORT).show();
                    }
                    else if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        wins = Integer.valueOf(intent.getExtras().getString("wins2"));
                        losses = Integer.valueOf(intent.getExtras().getString("losses2"));
                        tvWins.setText("wins " + wins);
                        tvLosses.setText("losses: " + losses);
                        SharedPreferences sp = getSharedPreferences("user",0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("wins", String.valueOf(wins));
                        editor.putString("losses", String.valueOf(losses));
                        editor.apply();
                    }

                }
        );
        tvWins.setText("wins " + wins);
        tvLosses.setText("losses: " + losses);
        rbEasy.setChecked(true);
    }
    private void initializeValues() {
        SharedPreferences sp = getSharedPreferences("user",0);
        if(sp.contains("wins")) {
            wins = Integer.valueOf(sp.getString("wins",null));
        }
        else {
            wins = 0;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("wins","0");
            editor.apply();
        }
        if(sp.contains("losses")) {
            losses = Integer.valueOf(sp.getString("losses",null));
        }
        else {
            losses = 0;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("losses","0");
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("wins", String.valueOf(wins));
        intent.putExtra("losses", String.valueOf(losses));
        intent.putExtra("difficulty-easy",rbEasy.isChecked());
        activityLauncher.launch(intent);
    }
}