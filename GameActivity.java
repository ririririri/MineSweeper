

package com.example.jianingsun.mineseeker;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.Delayed;

public class GameActivity extends AppCompatActivity {

    private int NUM_ROWS=5;
    private int NUM_COLS=5;
    private int minesRow=0;
    private int minesCol=0;
    private int NUM_MINES=5;

    Button[][] buttons=new Button[NUM_ROWS][NUM_COLS];
    int[][] minePosition=new int[NUM_ROWS][NUM_COLS];
    int[][]mineValue=new int[NUM_ROWS][NUM_COLS];
    int scanCount=0;
    int mineCount=0;
    int selection1;
    int selection2;
    MediaPlayer mineSound;
    MediaPlayer scanSound;
    Animation animation ;
    int bestScore=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTableSize();
        setMines();
        setBestText();
        setTotalGames();
        setSounds();
        setMinesCount();
        setScansCount();
        populateBtns();

    }

    private void setTotalGames() {
        Intent intent=getIntent();
        int total=intent.getIntExtra("Total #games",0);
        TextView text=(TextView)findViewById(R.id.total);
        text.setText("Total #games: "+total);
    }

    private void setBestText() {
        Intent intent =getIntent();
        bestScore= intent.getIntExtra("Least Scans",0);
        TextView text=(TextView)findViewById(R.id.bestTxt);
        text.setText(bestScore+" scans.");

    }


    private void setSounds() {
        mineSound= MediaPlayer.create(this,R.raw.minee);
        scanSound=MediaPlayer.create(this,R.raw.scanner);
    }

    private void setScansCount() {
        TextView text=(TextView)findViewById(R.id.countingScans);
        text.setText("# Scan usd: "+scanCount);
    }

    private void setMinesCount() {
        TextView text =(TextView)findViewById(R.id.countingMines);
        text.setText("Found "+mineCount+" of "+NUM_MINES+" determinations.");
    }

    private void setAMine() {
        Random mineRow=new Random();
        Random mineCol=new Random();
        minesRow=mineRow.nextInt(NUM_ROWS);
        minesCol=mineCol.nextInt(NUM_COLS);
    }

    private void setMines() {

        NUM_MINES=OptionsActivity.getNumOfMines(this);
        minePosition=new int[NUM_ROWS][NUM_COLS];
        mineValue=new int[NUM_ROWS][NUM_COLS];
        for(int i=0;i<NUM_ROWS;i++) {
            for(int j=0;j<NUM_COLS;j++ ) {
                minePosition[i][j]=0;
                mineValue[i][j]=0;
            }
        }

        for(int i=0;i<NUM_MINES;) {
            setAMine();
            if (minePosition[minesRow][minesCol] != 1) {
                minePosition[minesRow][minesCol] = 1;
                mineValue[minesRow][minesCol] = 1;
                i += 1;
            }
        }
    }

    private void populateBtns() {
        TableLayout table=(TableLayout)findViewById(R.id.tableForButtons);
        for(int row=0;row<NUM_ROWS;row++){
            TableRow tableRow=new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col=0;col<NUM_COLS;col++){
                final int finalRow=row;
                final int finalCol=col;
                Button button=new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                button.setPadding(0,0,0,0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridBtnClicked(finalRow,finalCol);
                    }
                });
                tableRow.addView(button);
                buttons[row][col]=button;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridBtnClicked(finalRow,finalCol);
                    }
                });
            }
        }

    }

    private boolean isMine(int row,int col){
        boolean mine=false;
        if(minePosition[row][col]==1)
            mine=true;
        return mine;
    }
    private boolean scanned(int row,int col){
        boolean scan=false;
        if(mineValue[row][col]==8)
            scan=true;
        return scan;
    }
    private void gridBtnClicked(int row, int col) {
        if(isMine(row,col)) {
            mineSound.start();
            Button button = buttons[row][col];
            lockButtonSizes();
            int newWidth = button.getWidth();
            int newHeight = button.getHeight();
            Bitmap originalBitmp = BitmapFactory.decodeResource(getResources(), R.drawable.jjj);
            Bitmap scaledBitmp = Bitmap.createScaledBitmap(originalBitmp, newWidth, newHeight, true);
            Resources resources = getResources();
            button.setBackground(new BitmapDrawable(resources, scaledBitmp));
            minePosition[row][col]=0;
            mineCount+=1;

            setMinesCount();
            if(mineCount==NUM_MINES) {
                setNewBest();
                congratulate();
                saveScore();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
            }

        }
         else {
            scanningAnimation(row,col);
            int count=0;
            if(!scanned(row,col))
               scanSound.start();
            count= calculate(row,col);
            Button button=buttons[row][col];
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setText(""+count);
            if(!scanned(row,col))
                scanCount+=1;
            mineValue[row][col]=8;
            setScansCount();
        }
        updateUI();

    }

    private void setNewBest() {
       if(scanCount<bestScore) {
           TextView text = (TextView) findViewById(R.id.bestTxt);
           text.setText(scanCount + " scans.");
       }

    }

    private void saveScore() {
        SharedPreferences prefs = this.getSharedPreferences("Score", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(NUM_ROWS==4)
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
        editor.putInt(selection1+"#Scans"+selection2, scanCount);
        editor.apply();
    }

    public static int getScore(Context context,int selection1,int selection2) {
        SharedPreferences prefs = context.getSharedPreferences("Score", MODE_PRIVATE);
        return prefs.getInt(selection1+"#Scans"+selection2, 0);
    }

    private void alpha(int row,int col){
        animation=new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(300);
        buttons[row][col].startAnimation(animation);

    }

    private void scanningAnimation(final int row, final int col) {
        if(!scanned(row,col)){
        Handler handler1 = new Handler();
        for (int i =0;i<NUM_COLS;i++) {
            final int finalI = i;
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    alpha(row, finalI);
                }
            }, 100 * i);
        }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Handler handler1 = new Handler();
                    for (int i =0;i<NUM_ROWS;i++) {
                        final int finalI = i;
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                alpha(finalI,col);
                            }
                        }, 100 * i);
                    }
                }
            }, 1000);

      }
    }

    private void congratulate() {
        FragmentManager manager=getSupportFragmentManager();
        messageFragment dialog=new messageFragment();
        dialog.show(manager,"MessageDialog");
    }

    private int calculate(int row,int col) {
        int count=0;
        for(int i=0;i<NUM_COLS;i++){
            count+=minePosition[row][i];
        }
        for(int i=0;i<NUM_ROWS;i++){
            count+=minePosition[i][col];
        }
        return count;
    }

    private void updateUI() {
        for(int i=0;i<NUM_ROWS;i++){
            for(int j=0;j<NUM_COLS;j++){
                if(scanned(i,j)){
                    int count=0;
                    count=calculate(i,j);
                    Button button=buttons[i][j];
                    button.setText(""+count);
                }
            }
        }
    }

    private void lockButtonSizes() {
        for(int row=0;row<NUM_ROWS;row++){
            for(int col=0;col<NUM_COLS;col++){
                Button button=buttons[row][col];
                int width=button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);
                int height=button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }

    }

    private void setTableSize() {
        NUM_ROWS=OptionsActivity.getNumOfRows(this);
        NUM_COLS=OptionsActivity.getNumOfCols(this);
        buttons=new Button[NUM_ROWS][NUM_COLS];
        minePosition=new int[NUM_ROWS][NUM_COLS];
        mineValue=new int[NUM_ROWS][NUM_COLS];

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context,GameActivity.class);
    }

}




