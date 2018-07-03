package com.vlad.lesson5_maskaikin_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

public class MainActivity extends SuperMainActivity {

    private Button buttonActivity2;
    private Button buttonActivity3;


    public static Intent createStartIntentWithFlag(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static Intent createStartIntent(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    void setLayoutResource() {
        viewStub.setLayoutResource(R.layout.viewstub_activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonActivity2 = findViewById(R.id.viewStubButtonActivity2);
        buttonActivity3 = findViewById(R.id.viewStubButtonActivity3);

        buttonActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity2.createStartIntent(MainActivity.this));
            }
        });

        buttonActivity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity3.createStartIntent(MainActivity.this));
            }
        });

    }


}
