package com.vlad.lesson12_maskaikin.presentation.ui.bridgeinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.vlad.lesson12_maskaikin.R;
import com.vlad.lesson12_maskaikin.data.model.Bridge;
import com.vlad.lesson12_maskaikin.presentation.ui.base.BaseActivity;

import static com.vlad.lesson12_maskaikin.data.model.BridgeExtraKt.getImageStatusBridge;
import static com.vlad.lesson12_maskaikin.data.model.BridgeExtraKt.getTimeCloseBridge;

public class BridgeInfoActivity extends BaseActivity implements BridgesInfoMvpView {

    private Toolbar myToolbar;
    private Integer idBridge;

    private TextView textViewNameBridge;
    private ImageView imageViewBridge;
    private ImageView imageViewStatusBridge;
    private TextView textViewCloseTimeBridge;
    private TextView textViewDescriptionBridge;
    private Button buttonRetry;

    private ViewFlipper viewFlipper;

    private BridgesInfoPresenter bridgeInfoPresenter;

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    private static final String BASE_URL_BRIDGE = "http://gdemost.handh.ru/";

    private static final String BRIDGE_ID = "bridge_id";
    public static final Integer DEFAULT_VALUE = 0;

    public static Intent createStartIntent(Context context, int bridgeId) {
        Intent intent = new Intent(context, BridgeInfoActivity.class);
        intent.putExtra(BRIDGE_ID, bridgeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_info);
        bridgeInfoPresenter = getApplicationComponents().providePridgesInfoPresenter();
        bridgeInfoPresenter.attachView(this);

        myToolbar = findViewById(R.id.toolbar);
        textViewNameBridge = findViewById(R.id.textViewNameBridge);
        imageViewBridge = findViewById(R.id.imageViewBridge);
        imageViewStatusBridge = findViewById(R.id.imageViewStatusBridge);
        textViewCloseTimeBridge = findViewById(R.id.textViewCloseTimeBridge);
        textViewDescriptionBridge = findViewById(R.id.textViewDescriptionBridge);
        viewFlipper = findViewById(R.id.viewFlipper);
        buttonRetry = findViewById(R.id.buttonRetry);

        idBridge = getIntent().getIntExtra(BRIDGE_ID, DEFAULT_VALUE);
        buttonRetry.setOnClickListener(view -> bridgeInfoPresenter.getBridgeInfo(idBridge));

        bridgeInfoPresenter.onCreate();
    }

    @Override
    public void setToolbar() {
        myToolbar.setTitle(getString(R.string.replace_empty));
        myToolbar.setPadding(0, 22, 0, 0);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setStatusBarColorTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void setSystemUiVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onClickToolbar() {
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public int getIdBridge() {
        return getIntent().getIntExtra(BRIDGE_ID, DEFAULT_VALUE);
    }

    @Override
    public void showBridgeInfo(Bridge bridge) {
        viewFlipper.setDisplayedChild(VIEW_DATA);

        textViewNameBridge.setText(bridge.getName().replace(getString(R.string.replace_bridge), getString(R.string.replace_empty))
                .replace(getString(R.string.replace_Bridge), getString(R.string.replace_empty)));
        imageViewStatusBridge.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getImageStatusBridge(bridge)));
        textViewCloseTimeBridge.setText(getTimeCloseBridge(bridge));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescriptionBridge.setText(Html.fromHtml(bridge.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewDescriptionBridge.setText(Html.fromHtml(bridge.getDescription()));
        }
    }

    @Override
    public void showProgressView() {
        viewFlipper.setDisplayedChild(VIEW_LOADING);
    }

    @Override
    public void showLoadingError() {
        viewFlipper.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public void setImageBridge(Bridge bridge) {
        if (getImageStatusBridge(bridge) == R.drawable.ic_brige_late) {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.getPhotoClose()).into(imageViewBridge);
        } else {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.getPhotoOpen()).into(imageViewBridge);
        }
    }
}
