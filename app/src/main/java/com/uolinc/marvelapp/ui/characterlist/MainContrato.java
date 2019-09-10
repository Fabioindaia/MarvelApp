package com.uolinc.marvelapp.ui.characterlist;

import com.uolinc.marvelapp.model.Result;

import java.util.List;

public interface MainContrato {

    interface View{
        void initialize();
        void setRecyclerViewCharacter();
        void setSpinnerOrderBy();
        void loadCharacterList(List<Result> _characterList);
        void showCharacterDetailActivity(Result result, String urlImage);
        void showError();
        void setTotal (int total);
    }

    interface Presenter{
        void getData(int limit, int offset, String orderBy);
    }
}
