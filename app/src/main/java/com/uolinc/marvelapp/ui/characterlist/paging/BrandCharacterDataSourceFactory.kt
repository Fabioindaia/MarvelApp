package com.eokoe.smartfitcoach.purchase.units.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.uolinc.marvelapp.network.ApiCall
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation
import io.reactivex.disposables.CompositeDisposable

/**
 * Classe responsável por criar observador da paginação da lista de unidades
 *
 * @param api serviço para chamada da api
 * @param disposer responsável pela coleta do Rxjava
 * @param orderBy informa como deve ser ordenado a lista
 *
 */
class BrandCharacterDataSourceFactory(
        private val api: ApiCall,
        private val disposer: CompositeDisposable,
        private val orderBy: String
) : DataSource.Factory<Int, CharacterPresentation>() {

    val dataSourceLiveData = MutableLiveData<BrandCharacterDataSource>()

    override fun create(): DataSource<Int, CharacterPresentation> {
        val dataSource = BrandCharacterDataSource(
                api, disposer, orderBy
        )
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}