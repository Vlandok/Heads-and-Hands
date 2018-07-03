package com.vlad.lesson5_maskaikin_2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

public class Activity3 extends SuperMainActivity {

    public static Intent createStartIntentWithFlag(Context context) {

        Intent intent = new Intent(context, Activity3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static Intent createStartIntent(Context context) {

        Intent intent = new Intent(context, Activity3.class);
        return intent;
    }

    @Override
    void setLayoutResource() {
        viewStub.setLayoutResource(R.layout.viewstub_activity_3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
