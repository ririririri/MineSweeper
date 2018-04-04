package com.example.jianingsun.mineseeker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int gameCount;
    private int bestScore=0;
    int selection1;
    int selection2;
    int NUM_ROWS;
    int NUM_MINES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome();
        getBestScore();
        setupBestText();
        setupCounting();
        setupOptionsBtn();
        setupHelpBtn();
        setupStartBtn();
        setupBestText();
    }

    private void setupBestText() {
        TextView text=(TextView)findViewById(R.id.bestText);
        text.setText("Best Score: "+ bestScore+" scans.");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case 150:
                if (resultCode == Activity.RESULT_OK) {
                    clearGameCounts();
                    setupCounting();
                    clearBest();
                    getBestScore();
                    setupBestText();
                }
                else {
                    getBestScore();
                    setupBestText();
                }
        }
        switch (requestCode) {
            case 165:
                if (resultCode == Activity.RESULT_OK) {
                    getBestScore();
                    setupBestText();
                }
        }
    }

    private void clearBest() {
        SharedPreferences prefs = this.getSharedPreferences("Score", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for(int i=0;i<4;i++){
            for(int j=0;j<5;j++){
                editor.putInt(i+"#Scans"+j, 0);
            }
        }
        editor.apply();
    }


    private void setupCounting() {
        gameCount=getGameCounts(this);
        TextView text=(TextView)findViewById(R.id.countingGames);
        text.setText("#Game started: "+gameCount);
    }

    private void welcome() {
        Intent intent = WelcomeActivity.makeIntent(MainActivity.this);
        startActivity(intent);
    }

    private void setupStartBtn() {
        Button btn=(Button)findViewById(R.id.startBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameCount+=1;
                saveCounts();
                setupCounting();
                Intent intent=GameActivity.makeIntent(MainActivity.this);
                intent.putExtra("Least Scans",bestScore);
                intent.putExtra("Total #games",gameCount);
                startActivityForResult(intent,165,null);
            }
        });

    }

    private void saveCounts() {
        SharedPreferences prefs = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("number of games", gameCount);
        editor.apply();
    }


    public static int getGameCounts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPref", MODE_PRIVATE);
        if(prefs.contains("number of games"))
            return prefs.getInt("number of games", 0);
        else
            return 0;
    }
    public void clearGameCounts() {

        SharedPreferences prefs = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("number of games",0);
        editor.apply();

    }

    private void setupHelpBtn() {
        Button btn=(Button)findViewById(R.id.helpBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=HelpActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupOptionsBtn() {
        Button btn=(Button)findViewById(R.id.optBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=OptionsActivity.makeIntent(MainActivity.this);
                startActivityForResult(intent,150,null);
            }
        });
    }

    public void getBestScore() {
        NUM_ROWS=OptionsActivity.getNumOfRows(this);
        NUM_MINES=OptionsActivity.getNumOfMines(this);
        if(OptionsActivity.getNumOfRows(this)==4)
            selection1=0;
        else if(NUM_ROWS==5)
            selection1=1;
        else if (NUM_ROWS==6)
            selection1=2;
        else
            selection1=3;
        if(NUM_MINES==6)
            selection2=0;
        else if (NUM_MINES==10)
            selection2=1;
        else if (NUM_MINES==15)
            selection2=2;
        else if (NUM_MINES==20)
            selection2=3;
        else
            selection2=4;
        int best=GameActivity.getScore(this,selection1,selection2);
        if(best<bestScore||bestScore==0||best==0)
            bestScore=best;
    }


}
