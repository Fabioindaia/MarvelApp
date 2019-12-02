package com.uolinc.marvelapp.ui.characterlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter.createFromResource
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.snackbar.Snackbar
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.model.Result
import com.uolinc.marvelapp.ui.characterdetail.CharacterDetailActivity
import java.util.*

class MainActivity : AppCompatActivity(), MainContrato.View {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var spinnerOrderBy: Spinner
    private lateinit var recyclerViewCharacter: RecyclerView
    private lateinit var progressBarList: ProgressBar
    private lateinit var presenter: MainContrato.Presenter
    private val resultList = ArrayList<Result>()
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var orderBy = "name"

    /**
     * Controle do scroll do recyclerview
     */
    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            with(linearLayoutManager) {
                presenter.setParametersToGetData(childCount, itemCount, findFirstVisibleItemPosition(), orderBy)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        initialize()

        spinnerOrderBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                orderBy = presenter.setOrderby(position)
                resultList.clear()
                progressBarList.visibility = View.VISIBLE
                presenter.getData(20, 0, orderBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Instância a classe presenter
     * Altera o título da toolbar
     * Chama método para setar os parâmetros da recyclerview
     * Chama método para setar os dados do spinner
     */
    override fun initialize() {
        constraintLayout = findViewById(R.id.constraintLayout)
        spinnerOrderBy = findViewById(R.id.spinnerOrderBy)
        recyclerViewCharacter = findViewById(R.id.recyclerViewCharacter)
        progressBarList = findViewById(R.id.progressBarList)
        ButterKnife.bind(this)

        if (supportActionBar != null) {
            supportActionBar!!.title = resources.getString(R.string.character)
        }
        setSpinnerOrderBy()
        setRecyclerViewCharacter()

        presenter = MainPresenter(this, characterAdapter)
    }

    /**
     * Seta parâmetros da recyclerview
     */
    override fun setRecyclerViewCharacter() {
        linearLayoutManager = LinearLayoutManager(this)
        with(recyclerViewCharacter) {
            layoutManager = linearLayoutManager
            recyclerViewCharacter.setHasFixedSize(true)
            characterAdapter = CharacterAdapter(resultList, this@MainActivity)
            recyclerViewCharacter.adapter = characterAdapter
            recyclerViewCharacter.addOnScrollListener(recyclerViewOnScrollListener)

        }
    }

    /**
     * Seta dados da spinner
     */
    override fun setSpinnerOrderBy() {
        val orderByAdapter: ArrayAdapter<CharSequence> =
                createFromResource(this, R.array.order_by, R.layout.spinner_item)
        orderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrderBy.adapter = orderByAdapter
    }

    /**
     * Carrega a lista de personagens
     *
     * @param _characterList lista de personagem
     */
    override fun loadCharacterList(_characterList: List<Result>) {
        resultList.addAll(_characterList)
        characterAdapter.notifyDataSetChanged()
        progressBarList.visibility = View.GONE
    }

    /**
     * Abre a tela com os detalhes do personagem
     *
     * @param result retorno com os dados do webservice
     * @param urlImage url da imagem do personagem
     */
    override fun showCharacterDetailActivity(result: Result, urlImage: String) {
        val intent = Intent(this, CharacterDetailActivity::class.java)
        with(intent) {
            putExtra("name", result.name)
            putExtra("description", result.description)
            putExtra("urlImage", urlImage)
        }

        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        startActivity(intent, bundle)
    }

    /**
     * Mostra mensagem de erro para o usuário com possibilidade de tentar carregar a lista novamente
     */
    override fun showError(isLoading: Boolean, limit: Int, offset: Int) {
        progressBarList.visibility = View.GONE
        if (isLoading) {
            resultList.add(Result())
            val position = resultList.size - 1
            resultList.removeAt(position)
            characterAdapter.notifyItemRemoved(position)
        }

        val snackbar = Snackbar.make(constraintLayout, getString(R.string.load_error), Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.try_again)) {
            progressBarList.visibility = View.VISIBLE
            presenter.getData(limit, offset, orderBy)
            snackbar.dismiss()
        }

        val snackView = snackbar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val snackTextView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackTextView.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.black))
        val snackActionView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackActionView.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.white))

        snackbar.show()
    }
}
