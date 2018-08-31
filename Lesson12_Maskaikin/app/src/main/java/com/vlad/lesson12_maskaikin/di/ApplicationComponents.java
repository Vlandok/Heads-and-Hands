package com.vlad.lesson12_maskaikin.di;

import android.content.Context;

import com.vlad.lesson12_maskaikin.data.remote.ApiService;
import com.vlad.lesson12_maskaikin.domain.provider.BridgesProvider;
import com.vlad.lesson12_maskaikin.presentation.ui.bridgeinfo.BridgesInfoPresenter;
import com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist.BridgesAdapter;
import com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist.BridgesListPresenter;

public class ApplicationComponents {

    private static volatile ApplicationComponents instance;

    private final ApiService apiService;
    private Context context;


    private final BridgesProvider bridgesProvider;


    private ApplicationComponents(Context context) {
        this.context = context;
        this.apiService = ApiService.Creator.newApiService(context);

        this.bridgesProvider = new BridgesProvider(apiService);
    }

    public static ApplicationComponents getInstance(Context context) {
        ApplicationComponents localInstance = instance;
        if (localInstance == null) {
            synchronized (ApplicationComponents.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApplicationComponents(context);
                }
            }
        }
        return localInstance;
    }

    public ApiService provideApiService() {
        return apiService;
    }

    public Context provideContext() {
        return context;
    }

    public BridgesAdapter provideBridgesAdapter() {
        return new BridgesAdapter();
    }

    public BridgesListPresenter providePridgesListPresenter() {
        return new BridgesListPresenter(provideBridgesProvider());
    }

    public BridgesInfoPresenter providePridgesInfoPresenter() {
        return new BridgesInfoPresenter(provideBridgesProvider());
    }

    public BridgesProvider provideBridgesProvider() {
        return bridgesProvider;
    }
}
