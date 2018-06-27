package com.vlad.lesson5_maskaikin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonGoToActivity4;
    private Button buttonGoToActivity2;

    public final static String TIME_1 = "time";

    public static Intent createStartIntent(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGoToActivity4 = findViewById(R.id.button_go_to_activity4);
        buttonGoToActivity2 = findViewById(R.id.button_go_to_activity2);

        buttonGoToActivity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Activity4.createStartIntent(MainActivity.this, TIME_1, System.currentTimeMillis()));
            }
        });

        buttonGoToActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Activity2.createStartIntent(MainActivity.this));
            }
        });
    }
}
