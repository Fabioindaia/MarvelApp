package com.uolinc.marvelapp.ui.characterlist

import com.uolinc.marvelapp.model.Result

interface MainContrato {

    interface View {
        fun initialize()
        fun setRecyclerViewCharacter()
        fun setSpinnerOrderBy()
        fun loadCharacterList(_characterList: List<Result>)
        fun showCharacterDetailActivity(result: Result, urlImage: String)
        fun showError(isLoading: Boolean, limit: Int, offset: Int)
    }

    interface Presenter {
        fun setOrderby(position: Int): String
        fun setParametersToGetData(visibleItemCount: Int, totalItemCount: Int,
                                   firstVisibleItemPosition: Int, orderBy: String)

        fun getData(limit: Int, offset: Int, orderBy: String)
    }
}
