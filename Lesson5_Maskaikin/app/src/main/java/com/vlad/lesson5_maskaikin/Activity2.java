package com.vlad.lesson5_maskaikin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity {

    private Button buttonGoToActivity3;

    public static Intent createStartIntent(Context context) {
        Intent intent = new Intent(context, Activity2.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        buttonGoToActivity3 = findViewById(R.id.button_go_to_activity3);

        buttonGoToActivity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Activity3.createStartIntent(Activity2.this));
            }
        });
    }
}
