package com.uolinc.marvelapp.ui.characterlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter.createFromResource
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.network.NetworkState
import com.uolinc.marvelapp.network.Status
import com.uolinc.marvelapp.ui.characterdetail.CharacterDetailActivity
import com.uolinc.marvelapp.ui.characterlist.adapter.CharactersAdapter
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation

class CharactersActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var spinnerOrderBy: Spinner
    private lateinit var charactersList: RecyclerView
    private lateinit var progressBarList: ProgressBar

    private val viewModel: CharactersViewModel by lazy {
        ViewModelProvider(this).get(CharactersViewModel::class.java)
    }
    private lateinit var charactersAdapter: CharactersAdapter
    private var orderBy = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)

        initialize()
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Chama método para setar os parâmetros da recyclerview
     * Chama método para setar os dados do spinner
     */
    private fun initialize() {
        constraintLayout = findViewById(R.id.constraintLayout)
        spinnerOrderBy = findViewById(R.id.spn_order_by)
        charactersList = findViewById(R.id.characters_list)
        progressBarList = findViewById(R.id.pb_characters)
        ButterKnife.bind(this)

        supportActionBar?.title = resources.getString(R.string.character)
        setupViews()
        configureSpinners()
    }

    /**
     * Seta adapter
     * Seta parâmetros do recyclerview
     */
    private fun setupViews() {
        charactersAdapter = CharactersAdapter({ viewModel.retry() }) {
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("character", it)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, bundle)
        }

        charactersList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = charactersAdapter
        }
    }

    /**
     * Seta dados da spinner
     */
    private fun configureSpinners() {
        val orderByAdapter: ArrayAdapter<CharSequence> =
                createFromResource(this, R.array.order_by, R.layout.spinner_item)
        orderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrderBy.adapter = orderByAdapter

        spinnerOrderBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                orderBy = if (position == 0) "name" else "modified"
                progressBarList.visibility = View.VISIBLE
                loadCharacters(orderBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    /**
     * Chama método do viewmodel para buscar personagens
     * Chama método para verificar o estado da rede
     *
     * @param orderBy informa como deve ser ordenado a lista
     * */
    private fun loadCharacters(orderBy: String) {
        viewModel.loadCharacters(orderBy).observe(this, Observer<PagedList<CharacterPresentation>> {
            Log.d("Marvel", "$it")
            charactersAdapter.submitList(it)
        })
        getNetworkState()
    }

    /**
     * Chama método do viewmodel
     * Seta layout da tela e refresh de acordo com estado de rede retornado
     * */
    private fun getNetworkState() {
        viewModel.getInitialState().observe(this, Observer<NetworkState> { networkState ->
            if (networkState?.status == Status.FAILED) finishedLoadind()
        })

        viewModel.getNetworkState().observe(this, Observer<NetworkState> { networkState ->
            charactersAdapter.setNetworkState(networkState)
            if (networkState?.status == Status.LOADED)
                handleSuccess(false)
            else if (networkState?.status == Status.EMPTY)
                handleSuccess(true)
        })
    }

    /**
     * Verifica se retornou vazio, se sim mostra mensagem para o usuário
     * Chama método para finalizar carregamento
     *
     * @param empty informa se retornou vazio
     * */
    private fun handleSuccess(empty: Boolean) {
        if (empty)
            Toast.makeText(this, R.string.content_empty, Toast.LENGTH_SHORT).show()
        finishedLoadind()
    }

    /**
     * Esconde a barra de progresso
     * */
    private fun finishedLoadind() {
        progressBarList.visibility = View.GONE
    }
}
