package com.uolinc.marvelapp.ui.characterlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.snackbar.Snackbar;
import com.uolinc.marvelapp.R;
import com.uolinc.marvelapp.model.Result;
import com.uolinc.marvelapp.ui.characterdetail.CharacterDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContrato.View {

    private ConstraintLayout constraintLayout;
    private Spinner spinnerOrderBy;
    private RecyclerView recyclerViewCharacter;
    private ProgressBar progressBarList;
    private MainContrato.Presenter presenter;
    private List<Result> resultList = new ArrayList<>();
    private CharacterAdapter characterAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String orderBy = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initialize();

        spinnerOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderBy = presenter.setOrderby(position);
                resultList.clear();
                progressBarList.setVisibility(View.VISIBLE);
                presenter.getData(20, 0, orderBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Instância a classe presenter
     * Altera o título da toolbar
     * Chama método para setar os parâmetros da recyclerview
     * Chama método para setar os dados do spinner
     */
    @Override
    public void initialize() {
        constraintLayout = findViewById(R.id.constraintLayout);
        spinnerOrderBy = findViewById(R.id.spinnerOrderBy);
        recyclerViewCharacter = findViewById(R.id.recyclerViewCharacter);
        progressBarList = findViewById(R.id.progressBarList);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.character));
        }
        setSpinnerOrderBy();
        setRecyclerViewCharacter();

        presenter = new MainPresenter(this, characterAdapter);
    }

    /**
     * Seta parâmetros da recyclerview
     */
    @Override
    public void setRecyclerViewCharacter() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCharacter.setLayoutManager(linearLayoutManager);
        recyclerViewCharacter.setHasFixedSize(true);
        characterAdapter = new CharacterAdapter(resultList, this);
        recyclerViewCharacter.setAdapter(characterAdapter);
        recyclerViewCharacter.addOnScrollListener(recyclerViewOnScrollListener);
    }

    /**
     * Seta dados da spinner
     */
    @Override
    public void setSpinnerOrderBy() {
        ArrayAdapter<CharSequence> orderByAdapter;
        orderByAdapter = ArrayAdapter.createFromResource(this, R.array.order_by, R.layout.spinner_item);
        orderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(orderByAdapter);
    }

    /**
     * Carrega a lista de personagens
     *
     * @param _resultList lista de personagem
     */
    @Override
    public void loadCharacterList(List<Result> _resultList) {
        runOnUiThread(() ->{
            resultList.addAll(_resultList);
            characterAdapter.notifyDataSetChanged();
            progressBarList.setVisibility(View.GONE);
        });
    }

    /**
     * Abre a tela com os detalhes do personagem
     *
     * @param result retorno com os dados do webservice
     * @param urlImage url da imagem do personagem
     */
    @Override
    public void showCharacterDetailActivity(Result result, String urlImage) {
        Intent intent = new Intent(this, CharacterDetailActivity.class);
        intent.putExtra("name", result.getName());
        intent.putExtra("description", result.getDescription());
        intent.putExtra("urlImage", urlImage);

        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    /**
     * Mostra mensagem de erro para o usuário com possibilidade de tentar carregar a lista novamente
     */
    @Override
    public void showError(boolean isLoading, int limit, int offset) {
        runOnUiThread(() -> {
            progressBarList.setVisibility(View.GONE);
            if (isLoading) {
                resultList.add(new Result());
                int position = resultList.size() - 1;
                resultList.remove(position);
                characterAdapter.notifyItemRemoved(position);
            }

            Snackbar snackbar = Snackbar.make(constraintLayout, getString(R.string.load_error), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.try_again), (View v) -> {
                progressBarList.setVisibility(View.VISIBLE);
                presenter.getData(limit, offset, orderBy);
                snackbar.dismiss();
            });

            View snackView = snackbar.getView();
            snackView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            TextView snackTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            snackTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
            TextView snackActionView = snackView.findViewById(com.google.android.material.R.id.snackbar_action);
            snackActionView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));

            snackbar.show();
        });
    }

    /**
     * Controle do scroll do recyclerview
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            presenter.scrolledRecyclerViewCharacter(linearLayoutManager, orderBy);
        }
    };
}
