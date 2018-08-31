package com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.ViewFlipper;

import java.util.List;

import com.vlad.lesson12_maskaikin.R;
import com.vlad.lesson12_maskaikin.data.model.Bridge;
import com.vlad.lesson12_maskaikin.presentation.ui.base.BaseActivity;
import com.vlad.lesson12_maskaikin.presentation.ui.bridgeinfo.BridgeInfoActivity;

public class BridgesListActivity extends BaseActivity implements BridgesListMvpView {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    private ViewFlipper viewFlipper;
    private Button buttonRetry;
    private RecyclerView recyclerView;
    private Toolbar myToolbar;

    private BridgesListPresenter bridgesListPresenter;
    private BridgesAdapter bridgesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridges_list);

        bridgesListPresenter = getApplicationComponents().providePridgesListPresenter();
        bridgesAdapter = getApplicationComponents().provideBridgesAdapter();

        bridgesListPresenter.attachView(this);

        viewFlipper = findViewById(R.id.viewFlipper);
        buttonRetry = findViewById(R.id.buttonRetry);
        recyclerView = findViewById(R.id.recyclerView);
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        buttonRetry.setOnClickListener(view -> bridgesListPresenter.getBridges());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bridgesAdapter);

        bridgesListPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        bridgesListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;

    }

    @Override
    public void showLoadingError() {
        viewFlipper.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public void showBridges(List<Bridge> bridges) {
        viewFlipper.setDisplayedChild(VIEW_DATA);
        bridgesAdapter.setBridges(bridges);
        }

    @Override
    public void showProgressView() {
        viewFlipper.setDisplayedChild(VIEW_LOADING);

    }

    @Override
    public void onBridgeClick() {
        bridgesAdapter.setOnBridgeClickListener(bridge -> startActivity(BridgeInfoActivity.createStartIntent(BridgesListActivity.this, bridge.getId())));
    }
}
