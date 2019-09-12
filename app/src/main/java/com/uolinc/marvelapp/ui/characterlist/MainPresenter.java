package com.uolinc.marvelapp.ui.characterlist;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.uolinc.marvelapp.model.ReturnData;
import com.uolinc.marvelapp.network.RetrofitConfig;
import com.uolinc.marvelapp.util.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContrato.Presenter {

    private MainContrato.View view;
    private CharacterAdapter characterAdapter;

    private int totalLoad = 20, offset = 0, totalRecord;
    private boolean isLoading = true, isLastPage = false;

    MainPresenter(MainContrato.View view, CharacterAdapter characterAdapter){
        this.view = view;
        this.characterAdapter = characterAdapter;
    }

    /**
     * Verificar a opção selecionada para ordenação da lista
     *
     * @param position posição do item selecionado
     * @return texto para ordenar
     */
    @Override
    public String setOrderby(int position) {
        if (position == 0) {
            return "name";
        }else{
            return "modified";
        }
    }

    /**
     * Faz a chamada para carregar os personagens
     *
     * @param limit quantidade total de registros por página
     * @param offset quantidade total para carregar
     * @param orderBy opção de ordenação da lista
     */
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
                        setTotalRecord(response.body().getData().getTotal());
                        view.loadCharacterList(response.body().getData().getResults());
                    }else{
                        view.showError(isLoading, limit, offset);
                    }
                } else {
                    view.showError(isLoading, limit, offset);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                view.showError(isLoading, limit, offset);
            }
        });
    }


    /**
     * Faz a chamada para carregar novos personagens quando atingir o final da lista
     *
     * @param linearLayoutManager linearlayoutmanager
     * @param orderBy opção de ordenação da lista
     */
    @Override
    public void scrolledRecyclerViewCharacter(LinearLayoutManager linearLayoutManager,
                                              String orderBy) {
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            int LIMIT = 20;
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= LIMIT) {

                isLoading = true;
                if (totalRecord - totalLoad >= LIMIT) {
                    totalLoad += LIMIT;
                }else{
                    totalLoad += (totalRecord - totalLoad);
                    isLastPage = true;
                }
                offset += LIMIT;
                getData(LIMIT, offset, orderBy);
            }
        }
    }

    /**
     * Seta a quantidade total de personagens do webservice
     *
     * @param totalRecord quantidade total de personagens
     */
    private void setTotalRecord(int totalRecord) {
        characterAdapter.setTotalRecord(totalRecord);
        this.totalRecord = totalRecord;
    }
}
