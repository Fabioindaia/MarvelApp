package com.uolinc.marvelapp.ui.characterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.uolinc.marvelapp.ui.characterlist.paging.BrandCharacterDataSource
import com.uolinc.marvelapp.ui.characterlist.paging.BrandCharacterDataSourceFactory
import com.uolinc.marvelapp.network.NetworkState
import com.uolinc.marvelapp.network.RetrofitConfig
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation
import io.reactivex.disposables.CompositeDisposable

class CharactersViewModel : ViewModel() {
    private val pageSize = 20
    private val numberElementsInitialLoad = pageSize * 2
    private val scrollAdapterNullObject = false
    private lateinit var dataSourceFactory: BrandCharacterDataSourceFactory
    private val config =
            PagedList.Config.Builder()
                    .setPageSize(pageSize)
                    .setInitialLoadSizeHint(numberElementsInitialLoad) //Quantidade de elementos para carregar no load inicial (padrão pagesize * 3)
                    .setEnablePlaceholders(scrollAdapterNullObject) //Se true = Quando fazer o scroll vai deixar o objeto que será exibido no adapter como null
                    .build()
    private lateinit var characters: LiveData<PagedList<CharacterPresentation>>
    private val compositeDisposable = CompositeDisposable()

    /**
     * Criar factory para paginação
     * Seta variável de paginação de personagens
     *
     * @param orderBy informa como deve ser ordenado a lista
     * */
    fun loadCharacters(orderBy: String): LiveData<PagedList<CharacterPresentation>> {
        dataSourceFactory =
                BrandCharacterDataSourceFactory(RetrofitConfig().apiCall(), compositeDisposable, orderBy)
        characters = LivePagedListBuilder<Int, CharacterPresentation>(dataSourceFactory, config).build()
        return characters
    }

    /**
     * Seta estado inicial da rede
     * */
    fun getInitialState(): LiveData<NetworkState> =
            Transformations.switchMap<BrandCharacterDataSource, NetworkState>(
                    dataSourceFactory.dataSourceLiveData
            ) { it.initialState }

    /**
     * Seta estado da rede
     * */
    fun getNetworkState(): LiveData<NetworkState> =
            Transformations.switchMap<BrandCharacterDataSource, NetworkState>(
                    dataSourceFactory.dataSourceLiveData
            ) { it.networkState }

    /**
     * Chama método para nova tentativa de busca de personagens
     * */
    fun retry() {
        dataSourceFactory.dataSourceLiveData.value!!.retry()
    }

    /**
     * Libera recursos e threads alocadas
     * */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}