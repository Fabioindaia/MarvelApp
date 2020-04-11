package com.eokoe.smartfitcoach.purchase.units.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.uolinc.marvelapp.network.ApiCall
import com.uolinc.marvelapp.network.NetworkState
import com.uolinc.marvelapp.ui.characterlist.mapper.DataCharacterMapper
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation
import com.uolinc.marvelapp.util.Keys
import com.uolinc.marvelapp.util.Tools
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 * Classe responsável pela paginação da lista de unidades
 *
 * @param api serviço para chamada da api
 * @param disposer responsável pela coleta do Rxjava
 * @param orderBy informa como deve ser ordenado a lista
 */
class BrandCharacterDataSource(
        private val api: ApiCall,
        private val disposer: CompositeDisposable,
        private val orderBy: String
) : PageKeyedDataSource<Int, CharacterPresentation>() {

    val initialState = MutableLiveData<NetworkState>()
    val networkState = MutableLiveData<NetworkState>()

    /**
     * Manter referência completa para o evento de nova tentativa
     */
    private var retryCompletable: Completable? = null

    /**
     * Faz nova tentativa de busca de unidades
     */
    fun retry() {
        if (retryCompletable != null) {
            disposer.add(
                    retryCompletable!!
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ }, { Log.d("Marver", "Error -> $it") })
            )
        }
    }

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, CharacterPresentation>
    ) {
        val numberOfItems = params.requestedLoadSize
        createObservable(0, 1, numberOfItems, params, callback, null, null)
    }

    override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, CharacterPresentation>
    ) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        createObservable(page, page + 1, numberOfItems, null, null, params, callback)
    }

    override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, CharacterPresentation>
    ) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        createObservable(page, page - 1, numberOfItems, null, null, params, callback)
    }

    /**
     * Cria o observable da chamada de busca de unidades do repositório,
     * Mapeia o retorno para apresentação na lista de unidades e
     * Seta o estado de rede conforme retorno do repositório
     *
     * @param requestedPage página requisitada
     * @param adjacentPage nova página requisita
     * @param initialParams parâmetros do load inicial
     * @param initialCallback retorno do load inicial
     * @param params parâmetros do load anterior/próximo
     * @param callback retorno do load anterior/próximo
     */
    private fun createObservable(
            requestedPage: Int,
            adjacentPage: Int,
            requestedLoadSize: Int,
            initialParams: LoadInitialParams<Int>?,
            initialCallback: LoadInitialCallback<Int, CharacterPresentation>?,
            params: LoadParams<Int>?,
            callback: LoadCallback<Int, CharacterPresentation>?
    ) {
        if (initialParams != null) initialState.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        val ts = Tools.ts
        val disposable = api.listCharacter(ts, Keys.apiKey, Tools.getHash(ts), requestedPage * requestedLoadSize, orderBy)
                .map {
                    DataCharacterMapper().fromResponse(it)
                }
                .subscribe({ response ->
                    setRetry(null)
                    if (response.characters.isNullOrEmpty() && requestedPage == 1) {
                        if (initialParams != null) initialState.postValue(NetworkState.EMPTY)
                        networkState.postValue(NetworkState.EMPTY)
                    } else {
                        if (initialParams != null) initialState.postValue(NetworkState.LOADED)
                        networkState.postValue(NetworkState.LOADED)
                    }
                    initialCallback?.onResult(
                            response.characters,
                            null,
                            adjacentPage
                    )
                    callback?.onResult(response.characters, adjacentPage)
                }, {
                    Log.e("Marvel", "Error -> $it")
                    setRetry(Action {
                        if (initialCallback != null) {
                            loadInitial(initialParams!!, initialCallback)
                        } else {
                            loadAfter(params!!, callback!!)
                        }
                    })
                    val error = NetworkState.error(it.toString())
                    if (initialParams != null) initialState.postValue(error)
                    networkState.postValue(error)
                })
        disposer.add(disposable)
    }

    /**
     * Seta se foi completado a nota tentativa
     *
     * @param action informa se existe ação de nova tentativa
     */
    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
}