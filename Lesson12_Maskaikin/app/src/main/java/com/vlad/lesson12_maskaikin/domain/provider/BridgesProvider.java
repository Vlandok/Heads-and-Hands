package com.vlad.lesson12_maskaikin.domain.provider;

import java.util.List;

import io.reactivex.Single;
import com.vlad.lesson12_maskaikin.data.model.Bridge;
import com.vlad.lesson12_maskaikin.data.model.BridgeResponse;
import com.vlad.lesson12_maskaikin.data.remote.ApiService;

public class BridgesProvider {

    private final ApiService apiService;

    public BridgesProvider(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<List<Bridge>> getBridges() {
        return apiService.getBridges()
                .map(BridgeResponse::getBridges);
    }

    public Single<Bridge> getBridgeInfo(int id) {
        return apiService.getBridgeInfo(id);
    }

}
