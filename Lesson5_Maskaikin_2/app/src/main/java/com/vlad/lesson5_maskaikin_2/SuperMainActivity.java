package com.vlad.lesson5_maskaikin_2;


import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SuperMainActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected NavigationView navigationView;
    protected DrawerLayout mDrawerLayout;

    protected void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isTaskRoot()) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24_px);
        }
    }

    protected void setNavigationView() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem != null) {
                    int id = menuItem.getItemId();


                    switch (id) {
                        case R.id.mainActivity:
                            startActivity(MainActivity.createStartIntentWithFlag(getApplicationContext()));
                            mDrawerLayout.closeDrawers();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.activity_2:
                            startActivity(Activity2.createStartIntentWithFlag(getApplicationContext()));
                            mDrawerLayout.closeDrawers();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.activity_3:
                            startActivity(Activity3.createStartIntentWithFlag(getApplicationContext()));
                            mDrawerLayout.closeDrawers();
                            overridePendingTransition(0, 0);
                            return true;
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (isTaskRoot()) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else {
            finish();
            return true;
        }
    }
}





