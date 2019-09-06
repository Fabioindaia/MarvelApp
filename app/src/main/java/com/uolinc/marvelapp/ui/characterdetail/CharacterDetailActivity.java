package com.uolinc.marvelapp.ui.characterdetail;

import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.uolinc.marvelapp.R;

public class CharacterDetailActivity extends AppCompatActivity implements CharacterDetailContrato.View {

    private SimpleDraweeView imgCharacter;
    private TextView txtName;
    private TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_character_detail);
        initialize();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     * @return verdadeiro
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     */
    @Override
    public void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        imgCharacter = findViewById(R.id.imgCharacter);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        uploadData();
    }

    /**
     * Carrega os dados do personagem
     */
    private void uploadData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgCharacter.setImageURI(bundle.getString("urlImage"));
            txtName.setText(bundle.getString("name"));
            txtDescription.setText(bundle.getString("description"));
        }
    }
}
