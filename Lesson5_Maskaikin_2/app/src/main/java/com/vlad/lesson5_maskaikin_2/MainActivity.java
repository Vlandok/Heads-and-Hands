package com.vlad.lesson5_maskaikin_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends SuperMainActivity {

    private ViewStub viewStub;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_main);

        setToolbar();
        setNavigationView();

        viewStub = findViewById(R.id.viewStub);
        viewStub.setLayoutResource(R.layout.viewstub_activity_main);
        viewStub.inflate();

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
