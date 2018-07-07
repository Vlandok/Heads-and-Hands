package com.vlad.lesson6_maskaikin;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GetStringFromClickedImage {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawerLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        container = findViewById(R.id.container);

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d("АГА", "onPageSelected, position = " + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.container, Fragment1.getInstance());
        fragmentTransaction.commit();

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.search_menu:
                        Toast toastSearchMenu = Toast.makeText(MainActivity.this, R.string.search, Toast.LENGTH_SHORT);
                        toastSearchMenu.show();
                        return true;
                    case R.id.extra_menu_item1:
                        Toast toastExtraMenuItem1 = Toast.makeText(MainActivity.this, R.string.extra_menu_item_1, Toast.LENGTH_SHORT);
                        toastExtraMenuItem1.show();
                        return true;
                    case R.id.extra_menu_item2:
                        Toast toastExtraMenuItem2 = Toast.makeText(MainActivity.this, R.string.extra_menu_item_2, Toast.LENGTH_SHORT);
                        toastExtraMenuItem2.show();
                        return true;
                    case R.id.extra_menu_item3:
                        Toast toastExtraMenuItem3 = Toast.makeText(MainActivity.this, R.string.extra_menu_item_3, Toast.LENGTH_SHORT);
                        toastExtraMenuItem3.show();
                        return true;

                }
                return false;

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.mainActivity:
                        mDrawerLayout.closeDrawers();
                        Toast toastActivityMain = Toast.makeText(MainActivity.this, R.string.activity_main, Toast.LENGTH_SHORT);
                        toastActivityMain.show();
                        return true;
                    case R.id.activity_2:
                        mDrawerLayout.closeDrawers();
                        Toast toastActivity2 = Toast.makeText(MainActivity.this, R.string.activity_2, Toast.LENGTH_SHORT);
                        toastActivity2.show();
                        return true;
                    case R.id.activity_3:
                        mDrawerLayout.closeDrawers();
                        Toast toastActivity3 = Toast.makeText(MainActivity.this, R.string.activity_3, Toast.LENGTH_SHORT);
                        toastActivity3.show();
                        return true;
                }

                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                item.setChecked(true);
                if (item.isChecked()) {
                    toolbar.setTitle(item.getTitle());
                }

                fragmentTransaction = fragmentManager.beginTransaction();

                switch (id) {
                    case R.id.bottom_navigation_item_one:
                        fragmentTransaction.replace(R.id.container, Fragment1.getInstance());
                        break;
                    case R.id.bottom_navigation_item_two:
                        fragmentTransaction.replace(R.id.container, Fragment2.getInstance());
                        break;
                    case R.id.bottom_navigation_item_three:
                        fragmentTransaction.replace(R.id.container, Fragment3.getInstance());
                        break;
                }

                fragmentTransaction.commit();


                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        getMenuInflater().inflate(R.menu.extra_menu, menu);
        return true;
    }


    @Override
    public void GetStringFromClickedImage(String data) {
        Toast toastTextImage = Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT);
        toastTextImage.show();
    }
}
