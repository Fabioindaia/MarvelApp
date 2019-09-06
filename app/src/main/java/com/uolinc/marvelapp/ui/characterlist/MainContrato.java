package com.uolinc.marvelapp.ui.characterlist;

import com.uolinc.marvelapp.model.Result;

import java.util.ArrayList;

public interface MainContrato {

    interface View{
        void initialize();
        void setRecyclerViewCharacter();
        void setSpinnerOrderBy();
        void loadCharacterList(ArrayList<Result> _characterArrayList);
        void showCharacterDetailActivity(Result result, String urlImage);
        void showError();
        void setTotal (int total);
    }

    interface Presenter{
        void getData(int limit, int offset, String orderBy);
    }
}
