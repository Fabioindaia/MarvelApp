package com.uolinc.marvelapp.ui.characterlist;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.uolinc.marvelapp.model.Result;

import java.util.List;

public interface MainContrato {

    interface View{
        void initialize();
        void setRecyclerViewCharacter();
        void setSpinnerOrderBy();
        void loadCharacterList(List<Result> _characterList);
        void showCharacterDetailActivity(Result result, String urlImage);
        void showError(boolean isLoading, int limit, int offset);
    }

    interface Presenter{
        String setOrderby(int position);
        void getData(int limit, int offset, String orderBy);
        void scrolledRecyclerViewCharacter(LinearLayoutManager linearLayoutManager, String orderBy);
    }
}
