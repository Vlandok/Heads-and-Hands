package com.vlad.lesson12_maskaikin.presentation.ui.bridgeinfo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.vlad.lesson12_maskaikin.domain.provider.BridgesProvider;
import com.vlad.lesson12_maskaikin.presentation.ui.base.BasePresenter;
import com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist.BridgesListMvpView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BridgesInfoPresenter extends BasePresenter<BridgesInfoMvpView> {

    private int idBridge;

    @NonNull
    private final BridgesProvider bridgesProvider;

    @Nullable
    private Disposable disposable;

    public BridgesInfoPresenter(@NonNull BridgesProvider bridgesProvider) {
        this.bridgesProvider = bridgesProvider;
    }

    public void onCreate() {
        getMvpView().setToolbar();
        getMvpView().setStatusBarColorTransparent();
        getMvpView().setSystemUiVisibility();
        getMvpView().onClickToolbar();
        idBridge = getMvpView().getIdBridge();
        checkViewAttached();
        getBridgeInfo(idBridge);
    }

    public void getBridgeInfo(Integer idBridge) {
        checkViewAttached();
        getMvpView().showProgressView();
        disposable = bridgesProvider.getBridgeInfo(idBridge)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bridge -> {
                            getMvpView().setImageBridge(bridge);
                            getMvpView().showBridgeInfo(bridge);
                        },
                        error -> {
                            error.printStackTrace();
                            getMvpView().showLoadingError();
                        });
    }

    @Override
    protected void doUnsubscribe() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
