package com.example.jianingsun.mineseeker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class OptionsActivity extends AppCompatActivity {

    int tablesRow=0;
    int tablesCol=0;
    int tableNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        setupSizeRadioBtn();
        setupNumRadioBtn();
        setupOkBtn();
        setupClearBtn();
    }

    private void setupClearBtn() {
        Button btn= (Button)findViewById(R.id.clear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setupOkBtn() {
        Button btn=(Button)findViewById(R.id.okBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupNumRadioBtn() {
        RadioGroup group = (RadioGroup) findViewById(R.id.numOfMines);
        int[] num = getResources().getIntArray(R.array.numberOfMines);
        for (int i = 0; i < num.length; i++) {
            final int number = num[i];
            RadioButton button = new RadioButton(this);
            button.setText(number + " mines.");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tableNum=number;
                    saveNumOfMines(number);
                }
            });
            group.addView(button);
            if (number == getNumOfMines(this)) {
                button.setChecked(true);
            }
        }
    }

    private void saveNumOfMines(int number) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefss", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Number of Mines", number);
        editor.apply();
    }

    public static int getNumOfMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefss", MODE_PRIVATE);
        return prefs.getInt("Number of Mines", 0);
    }

    private void setupSizeRadioBtn() {
        RadioGroup sizeGroup = (RadioGroup) findViewById(R.id.sizeRadio);
        int[] rows = getResources().getIntArray(R.array.boardRow);
        int[] cols = getResources().getIntArray(R.array.boardCol);
        for (int i = 0; i < rows.length; i++) {
            final int row = rows[i];
            final int col = cols[i];
            RadioButton button = new RadioButton(this);
            button.setText(row + " rows by " + col +" columns.");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tablesRow=row;
                    tablesCol=col;
                    saveTableSize(row,col);
                }
            });
            sizeGroup.addView(button);
            if (row == getNumOfRows(this)&&col==getNumOfCols(this)) {
                button.setChecked(true);
            }
        }
    }

    private void saveTableSize(int row, int col) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("num of rows", row);
        editor.putInt("num of cols",col);
        editor.apply();
    }
    public static int getNumOfRows(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("num of rows", 5);
    }
    public static int getNumOfCols(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("num of cols", 4);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

}
