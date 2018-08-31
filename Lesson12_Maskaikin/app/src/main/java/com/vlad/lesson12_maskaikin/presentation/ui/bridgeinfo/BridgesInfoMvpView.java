package com.vlad.lesson12_maskaikin.presentation.ui.bridgeinfo;

import android.view.Window;

import com.vlad.lesson12_maskaikin.data.model.Bridge;
import com.vlad.lesson12_maskaikin.presentation.ui.base.MvpView;

import java.util.List;

public interface BridgesInfoMvpView extends MvpView {
    void setToolbar();
    void setStatusBarColorTransparent();
    void setSystemUiVisibility();
    void onClickToolbar();
    int getIdBridge();
    void showBridgeInfo(Bridge bridge);
    void showProgressView();
    void showLoadingError();
    void setImageBridge(Bridge bridge);
}
