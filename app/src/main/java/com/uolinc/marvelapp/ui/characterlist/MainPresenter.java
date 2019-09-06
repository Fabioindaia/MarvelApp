package com.uolinc.marvelapp.ui.characterlist;

import com.uolinc.marvelapp.model.ReturnData;
import com.uolinc.marvelapp.network.RetrofitConfig;
import com.uolinc.marvelapp.util.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContrato.Presenter {
    private MainContrato.View view;

    MainPresenter(MainContrato.View view){
        this.view = view;
    }

    @Override
    public void getData(int limit, int offset, String orderBy) {
        String tS = Tools.getTs();
        Call<ReturnData> call = new RetrofitConfig().apiCall().listCharacter(tS, Tools.getApiKey(), Tools.getHash(tS), limit, offset, orderBy);
        //noinspection NullableProblems
        call.enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                 if (response.isSuccessful()) {
                    if (response.body() != null) {
                        view.setTotal(response.body().getData().getTotal());
                        view.loadCharacterList(response.body().getData().getResults());
                    }else{
                        view.showError();
                    }
                } else {
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                view.showError();
            }
        });
    }
}
