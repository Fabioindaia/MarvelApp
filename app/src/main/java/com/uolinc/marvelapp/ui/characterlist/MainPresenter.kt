package com.uolinc.marvelapp.ui.characterlist

import com.uolinc.marvelapp.network.RetrofitConfig
import com.uolinc.marvelapp.util.Tools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter internal constructor(
        private val view: MainContrato.View,
        private val characterAdapter: CharacterAdapter
) : MainContrato.Presenter {

    private var totalLoad = 20
    private var offset = 0
    private var totalRecord: Int = 0
    private var isLoading = true
    private var isLastPage = false
    private var retrofit = RetrofitConfig()

    /**
     * Verificar a opção selecionada para ordenação da lista
     *
     * @param position posição do item selecionado
     * @return texto para ordenar
     */
    override fun setOrderby(position: Int): String {
        return if (position == 0) {
            "name"
        } else {
            "modified"
        }
    }

    /**
     * Seta parâmetros e faz a chamada para carregar novos personagens quando atingir o final
     * da lista
     *
     * @param visibleItemCount quatidade de itens visiveis
     * @param totalItemCount total de itens na lista
     * @param firstVisibleItemPosition posição do primeiro item visivel na lista
     * @param orderBy opção de ordenação da lista
     */
    override fun setParametersToGetData(visibleItemCount: Int, totalItemCount: Int,
                                        firstVisibleItemPosition: Int, orderBy: String) {
        if (!isLoading && !isLastPage) {
            val limit = 20
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= limit) {

                isLoading = true
                if (totalRecord - totalLoad >= limit) {
                    totalLoad += limit
                } else {
                    totalLoad += totalRecord - totalLoad
                    isLastPage = true
                }
                offset += limit
                getData(limit, offset, orderBy)
            }
        }
    }

    /**
     * Faz a chamada para carregar os personagens
     *
     * @param limit quantidade total de registros por página
     * @param offset quantidade total para carregar
     * @param orderBy opção de ordenação da lista
     */
    override fun getData(limit: Int, offset: Int, orderBy: String) {
        val tS = Tools.ts
        GlobalScope.launch(Dispatchers.Main){
            try {
                val response = retrofit.apiCall().listCharacter(tS, Tools.apiKey, Tools.getHash(tS)!!, limit, offset, orderBy)
                setTotalRecord(response.data.total)
                view.loadCharacterList(response.data.results)
                isLoading = false
            } catch (e: Exception) {
                view.showError(isLoading, limit, offset)
            }
        }
    }

    /**
     * Seta a quantidade total de personagens do webservice
     *
     * @param totalRecord quantidade total de personagens
     */
    private fun setTotalRecord(totalRecord: Int) {
        characterAdapter.setTotalRecord(totalRecord)
        this.totalRecord = totalRecord
    }
}
