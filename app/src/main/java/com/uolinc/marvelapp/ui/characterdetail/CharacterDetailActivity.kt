package com.uolinc.marvelapp.ui.characterdetail

import android.os.Bundle
import android.transition.Explode
import android.view.MenuItem
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.uolinc.marvelapp.R

class CharacterDetailActivity : AppCompatActivity(), CharacterDetailContrato.View {

    private lateinit var imgCharacter: SimpleDraweeView
    private lateinit var txtName: TextView
    private lateinit var txtDescription: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_character_detail)
        initialize()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Explode()
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     * @return verdadeiro
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     */
    override fun initialize() {
        toolbar = findViewById(R.id.toolbar)
        imgCharacter = findViewById(R.id.imgCharacter)
        txtName = findViewById(R.id.txtName)
        txtDescription = findViewById(R.id.txtDescription)

        setSupportActionBar(toolbar)
        with(supportActionBar!!) {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        uploadData()
    }

    /**
     * Carrega os dados do personagem
     */
    private fun uploadData() {
        val bundle = intent.extras
        with(bundle!!) {
            imgCharacter.setImageURI(getString("urlImage"))
            txtName.text = getString("name")
            txtDescription.text = getString("description")
        }
    }
}
