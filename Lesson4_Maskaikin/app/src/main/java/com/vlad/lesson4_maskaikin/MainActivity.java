package com.vlad.lesson4_maskaikin;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vlad.lesson4_maskaikin.adapter.RVAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Snackbar snackbarItem;
    private ArrayList<BaseInfoItem> listBaseItems;
    private ArrayList<DetailInfoItem> listDetailItems;
    private LinearLayout lnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        lnr = findViewById(R.id.linerLayout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        final RVAdapter adapter = new RVAdapter(initializeDetailItems(), initializeBaseItems());

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            public int getSpanSize(int position) {
               return adapter.getSpanSize(position);
            }
        });


        recyclerView.setLayoutManager(gridLayoutManager);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(BaseInfoItem item) {

                    snackbarItem = Snackbar.make(lnr, item.getTitle(), Snackbar.LENGTH_LONG);
                    snackbarItem.show();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item_info) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.title_alert_info)
                            .setMessage(R.string.message_alert_info)
                            .setPositiveButton(R.string.positive_button_alert,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else if (item.getItemId() == R.id.item_home) {
                    Toast toastMenu = Toast.makeText(MainActivity.this, R.string.toast_text_home, Toast.LENGTH_SHORT);
                    toastMenu.show();
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_apartment_menu, menu);
        return true;
    }

    private ArrayList<BaseInfoItem> initializeBaseItems() {

        listBaseItems = new ArrayList<>(initializeDetailItems().size());


        listBaseItems.add(new BaseInfoItem(R.drawable.ic_uk_contacts, R.string.uk_contacts_title));
        listBaseItems.add(new BaseInfoItem(R.drawable.ic_request, R.string.request_title));
        listBaseItems.add(new BaseInfoItem(R.drawable.ic_about, R.string.about_title));

        return listBaseItems;
    }

    private ArrayList<DetailInfoItem> initializeDetailItems() {

        listDetailItems = new ArrayList<>();
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_bill, R.string.bill_title, R.string.bill_description, true));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_counter, R.string.counter_title, R.string.counter_description, true));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_installment, R.string.installment_title, R.string.installment_description, false));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_insurance, R.string.insurance_title, R.string.insurance_description, false));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_tv, R.string.tv_title, R.string.tv_description, false));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_homephone, R.string.homephone_title, R.string.homephone_description, false));
        listDetailItems.add(new DetailInfoItem(R.drawable.ic_guard, R.string.guard_title, R.string.guard_description, false));

        return listDetailItems;
    }
}
