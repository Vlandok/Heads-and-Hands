package com.vlad.lesson5_maskaikin;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;

public class Activity3 extends AppCompatActivity {

    private Button buttonGoToActivity5;
    private Button buttonGoToActivity1;
    private ConstraintLayout constraintLayout;

    private static final int REQUEST_CHECK = 104;


    public static Intent createStartIntent(Context context) {

        Intent intent = new Intent(context, Activity3.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        buttonGoToActivity1 = findViewById(R.id.button_go_to_activity1);
        buttonGoToActivity5 = findViewById(R.id.button_go_to_activity5);
        constraintLayout = findViewById(R.id.activity3_layout);

        buttonGoToActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.createStartIntent(Activity3.this));
            }
        });

        buttonGoToActivity5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Activity5.createStartIntent(Activity3.this), REQUEST_CHECK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CHECK && resultCode == RESULT_OK) {

            String text = data.getStringExtra(Activity5.CHECK_RESULT);
            Snackbar snackbarResult = Snackbar.make(constraintLayout, text, Snackbar.LENGTH_LONG);
            snackbarResult.show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
