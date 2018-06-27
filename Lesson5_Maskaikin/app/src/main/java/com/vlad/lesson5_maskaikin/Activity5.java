package com.vlad.lesson5_maskaikin;

import android.content.Context;
import android.content.Intent;
import android.nfc.TagLostException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity5 extends AppCompatActivity {

    private EditText editTextSend;
    private Button buttonDeliverResult;

    public final static String CHECK_RESULT = "text";

    public static Intent createStartIntent(Context context) {

        Intent intent = new Intent(context, Activity5.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        editTextSend = findViewById(R.id.editTextSend);
        buttonDeliverResult = findViewById(R.id.button_deliver_result);

        buttonDeliverResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra(CHECK_RESULT, editTextSend.getText().toString());
                if (editTextSend.getText().toString() == "") {
                    setResult(RESULT_CANCELED, resultIntent);
                }
                else {
                    setResult(RESULT_OK, resultIntent);
                }
                finish();

            }
        });


    }
}
