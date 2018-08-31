package com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist;

import java.util.List;

import com.vlad.lesson12_maskaikin.data.model.Bridge;
import com.vlad.lesson12_maskaikin.presentation.ui.base.MvpView;

public interface BridgesListMvpView extends MvpView {
    void showLoadingError();
    void showBridges(List<Bridge> bridges);
    void showProgressView();
    void onBridgeClick();
}
