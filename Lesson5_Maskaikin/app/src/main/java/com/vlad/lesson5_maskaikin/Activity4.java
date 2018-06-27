package com.vlad.lesson5_maskaikin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Activity4 extends AppCompatActivity {

    private Button buttonGoActivity4Again;
    private TextView textViewTime;
    private DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Date date;

    private final static String TIME_2 = "timeCheckAgain";

    public static Intent createStartIntent(Context context, String constant, Long time) {

        Intent intent = new Intent(context, Activity4.class);
        intent.putExtra(constant, time);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        buttonGoActivity4Again = findViewById(R.id.button_go_to_activity4_again);
        textViewTime = findViewById(R.id.text_view_time);

        buttonGoActivity4Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(createStartIntent(Activity4.this, TIME_2, System.currentTimeMillis()));
            }
        });

        Long getTime = getIntent().getLongExtra(MainActivity.TIME_1, -1);

        if (getTime != -1) {
            date = new Date(getTime);
            String dateFormatted = formatter.format(date);
            textViewTime.setText(String.valueOf(dateFormatted));
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Long getTime = intent.getLongExtra(TIME_2, -2);

        if (getTime != -2) {
            date = new Date(getTime);

            String dateFormatted = formatter.format(date);
            textViewTime.setText(String.valueOf(dateFormatted));
        }

    }
}
