package com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.vlad.lesson12_maskaikin.domain.provider.BridgesProvider;
import com.vlad.lesson12_maskaikin.presentation.ui.base.BasePresenter;

public class BridgesListPresenter extends BasePresenter<BridgesListMvpView> {

    @NonNull
    private final BridgesProvider bridgesProvider;

    @Nullable
    private Disposable disposable;

    public BridgesListPresenter(@NonNull BridgesProvider bridgesProvider) {
        this.bridgesProvider = bridgesProvider;
    }

    public void onCreate() {
        checkViewAttached();
        getBridges();
    }

    public void getBridges() {
        checkViewAttached();
        getMvpView().showProgressView();
        disposable = bridgesProvider.getBridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bridges -> {
                            getMvpView().showBridges(bridges);
                            getMvpView().onBridgeClick();
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
