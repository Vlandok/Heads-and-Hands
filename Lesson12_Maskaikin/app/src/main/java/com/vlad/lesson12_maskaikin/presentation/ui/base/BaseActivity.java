package com.vlad.lesson12_maskaikin.presentation.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vlad.lesson12_maskaikin.MyApplication;
import com.vlad.lesson12_maskaikin.di.ApplicationComponents;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ApplicationComponents getApplicationComponents() {
        return ((MyApplication) getApplication()).getApplicationComponents();
    }
}
